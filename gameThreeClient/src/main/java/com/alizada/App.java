package com.alizada;

import com.alizada.rabbitmq.RabbitMqConfig;
import com.alizada.rabbitmq.Receiver;
import com.alizada.model.Action;
import com.alizada.model.Player;
import com.alizada.rabbitmq.Sender;
import com.alizada.resolver.PlayerResolver;
import com.rabbitmq.client.Connection;

import java.util.Scanner;

public class App {
    public final static Scanner scanner = new Scanner(System.in);
    public static boolean autoGame = true;
    private static Player player = new Player();
    private static RabbitMqConfig rabbitMqConfig = new RabbitMqConfig();
    private static Connection connection = rabbitMqConfig.createConnection();
    private static Sender sender = new Sender(rabbitMqConfig, connection);
    private static PlayerResolver playerResolver = new PlayerResolver(sender);
    private static Receiver receiver = new Receiver(rabbitMqConfig,connection, playerResolver);

    public static void main( String[] args ) {
        System.out.println("Welcome to Game of Three");
        System.out.print("Add player name: ");
        String playerName = scanner.next();
        player.setName(playerName);
        startGame();
    }

    public static void startGame() {

        System.out.println("Create new game - 1");
        System.out.println("Join created games - 2");
        System.out.print("Action number: ");
        String actionNumber = scanner.next();

        if(actionNumber.equals("1")){
            selectGameMode();
            System.out.println("Creating..");
            player.setAction(Action.CREATE);
            sender.create("game", player);
            receiver.create(player.getName(), player);

        } else if(actionNumber.equals("2")){
            selectGameMode();
            System.out.println("Loading..");
            player.setAction(Action.SEARCH);
            sender.create("game", player);
            receiver.create(player.getName(), player);

        } else {
            System.out.println("--Not Found--");
            startGame();
        }
    }

    private static void selectGameMode() {
        System.out.println("--Select Game Mode--");
        System.out.println("Auto - 1");
        System.out.println("Manual - 2");
        System.out.print("Action number: ");
        String gameMode = scanner.next();
        if(gameMode.equals("1")){
            autoGame = true;
        }else if(gameMode.equals("2")){
            autoGame = false;
        } else {
            System.out.println("--Not Found--");
            selectGameMode();
        }
    }
}
