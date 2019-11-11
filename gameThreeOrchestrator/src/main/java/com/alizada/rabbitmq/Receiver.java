package com.alizada.rabbitmq;

import com.alizada.exception.RabbitMqException;
import com.alizada.model.Player;
import com.alizada.resolver.GameResolver;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.lang3.SerializationUtils;

public class Receiver {
    private Connection connection;
    private RabbitMqConfig rabbitMqConfig;
    private GameResolver gameResolver;

    public Receiver(RabbitMqConfig rabbitMqConfig, Connection connection, GameResolver gameResolver) {
        this.rabbitMqConfig = rabbitMqConfig;
        this.connection = connection;
        this.gameResolver = gameResolver;
    }

    public void create(String queue) {
        Channel channel = rabbitMqConfig.createChannel(connection);
        try {
            channel.queueDeclare(queue, false, false, false, null);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                Player receivedPlayedData = SerializationUtils.deserialize(delivery.getBody());
                gameResolver.resolve(receivedPlayedData);
            };

            channel.basicConsume(queue, true, deliverCallback, consumerTag -> {});
        }catch (Exception ex){
            throw new RabbitMqException("Can not create receiver", ex);
        }
    }
}
