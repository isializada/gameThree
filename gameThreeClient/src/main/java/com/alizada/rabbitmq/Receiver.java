package com.alizada.rabbitmq;

import com.alizada.exception.RabbitMqException;
import com.alizada.model.Player;
import com.alizada.resolver.PlayerResolver;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.lang3.SerializationUtils;

public class Receiver {
    private Connection connection;
    private RabbitMqConfig rabbitMqConfig;
    private PlayerResolver playerResolver;

    public Receiver(RabbitMqConfig rabbitMqConfig, Connection connection, PlayerResolver playerResolver) {
        this.rabbitMqConfig = rabbitMqConfig;
        this.connection = connection;
        this.playerResolver = playerResolver;

    }

    public void create(String queue, Player player) {
        Channel channel = rabbitMqConfig.createChannel(connection);
        try {
            channel.queueDeclare(queue, false, false, false, null);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Player receivedPlayer = SerializationUtils.deserialize(delivery.getBody());
                playerResolver.resolve(receivedPlayer, player);
            };

            channel.basicConsume(queue, true, deliverCallback, consumerTag -> {});
        }catch (Exception ex){
            throw new RabbitMqException("Can not create receiver", ex);
        }
    }

}
