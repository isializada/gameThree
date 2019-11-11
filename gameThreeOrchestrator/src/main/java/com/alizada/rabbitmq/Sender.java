package com.alizada.rabbitmq;

import com.alizada.exception.RabbitMqException;
import com.alizada.model.Player;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.apache.commons.lang3.SerializationUtils;

public class Sender {
    private RabbitMqConfig rabbitMqConfig;
    private Connection connection;

    public Sender(RabbitMqConfig rabbitMqConfig, Connection connection) {
        this.rabbitMqConfig = rabbitMqConfig;
        this.connection = connection;
    }

    public void create(String queue, Player player) {
        Channel channel = rabbitMqConfig.createChannel(connection);
        try {
            channel.queueDeclare(queue, false, false,false,null);
            channel.basicPublish("", queue,null, SerializationUtils.serialize(player));
        } catch (Exception ex){
            throw new RabbitMqException("Can not create sender", ex);
        }
    }
}
