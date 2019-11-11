package com.alizada;

import com.alizada.rabbitmq.RabbitMqConfig;
import com.alizada.rabbitmq.Receiver;
import com.alizada.rabbitmq.Sender;
import com.alizada.resolver.GameResolver;
import com.rabbitmq.client.Connection;

import java.util.HashMap;
import java.util.Map;

public class App
{
    public static Map<String, String> playersMap = new HashMap<>();

    private static RabbitMqConfig rabbitMqConfig = new RabbitMqConfig();
    private static Connection connection = rabbitMqConfig.createConnection();
    private static Sender sender = new Sender(rabbitMqConfig, connection);
    private static GameResolver gameResolver = new GameResolver(sender);
    private static Receiver receiver = new Receiver(rabbitMqConfig,connection,gameResolver);

    public static void main( String[] args ) {
        receiver.create("game");
    }
}
