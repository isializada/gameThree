package com.alizada.rabbitmq;

import com.alizada.exception.RabbitMqException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqConfig {
    public Connection createConnection() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try{
            return connectionFactory.newConnection();
        }catch (Exception e){
            throw new RabbitMqException("Can not create connection", e);
        }
    }

    public Channel createChannel(Connection connection) {
        try{
            return connection.createChannel();
        }catch (Exception e){
            throw new RabbitMqException("Can not create channel", e);
        }
    }

}
