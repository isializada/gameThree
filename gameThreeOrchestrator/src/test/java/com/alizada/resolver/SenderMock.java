package com.alizada.resolver;

import com.alizada.model.Player;
import com.alizada.rabbitmq.RabbitMqConfig;
import com.alizada.rabbitmq.Sender;
import com.rabbitmq.client.Connection;

public class SenderMock extends Sender {
    public SenderMock(RabbitMqConfig rabbitMqConfig, Connection connection) {
        super(rabbitMqConfig, connection);
    }

    @Override
    public void create(String queue, Player player) {

    }
}
