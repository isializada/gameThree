package com.alizada.resolver;

import com.alizada.App;
import com.alizada.model.Action;
import com.alizada.model.Player;
import com.alizada.rabbitmq.Sender;

import java.util.ArrayList;
import java.util.Random;

public class PlayerResolver {
    private Sender sender;

    public PlayerResolver(Sender sender) {
        this.sender = sender;
    }

    public void resolve(Player receivedPlayer, Player currentPlayer) {
        final Action receivedAction = receivedPlayer.getAction();
        final String receivedPlayerName = receivedPlayer.getName();

        switch (receivedAction){
            case CREATED:
                System.out.println("Waiting for other player..");
                break;
            case RESULT:
                joinGame(currentPlayer, receivedPlayer.getGames());
                break;
            case JOINED:
                startGame(currentPlayer, receivedPlayerName);
                break;
            case PLAYING:
                System.out.println(receivedPlayerName + " sent the value " + receivedPlayer.getValue());
                addNextNumber(currentPlayer, receivedPlayer);
                break;
            case FINISH:
                finishGame(currentPlayer.getName(), receivedPlayerName);
        }
    }


    private void finishGame(String currentPlayerName, String receivedPlayerName) {
        if(currentPlayerName.equals(receivedPlayerName)){
            System.out.println("WON!");
            App.startGame();
        } else {
            System.out.println("LOST!");
            App.startGame();
        }
    }

    private void addNextNumber(Player player, Player receivedPlayer) {
        player.setValue(addNumber(receivedPlayer.getValue())/3);
        sender.create("game", player);
        System.out.println("Sent!");
    }

    private double addNumber(Double receivedNumber) {

        if(App.autoGame) {
            Double foundNumber = findNextNumber(receivedNumber);
            System.out.println("Next number: " + foundNumber.toString());
            return foundNumber;
        }else {
            while (true) {
                System.out.print("Add your number: ");
                String number = App.scanner.next();
                if (validateNumber(number, receivedNumber)) {
                    return Double.valueOf(number);
                } else {
                    System.out.println(number + " is not correct!");
                }
            }
        }
    }

    private void startGame(Player player, String receivedPlayerName) {
        player.setAction(Action.PLAYING);
        if(receivedPlayerName.equals(player.getName())){
            System.out.println("Joined the game..");
            System.out.println("Waiting for partner answer..");

        }else {
            System.out.println(receivedPlayerName + " is joined your game!");
            player.setValue(addNumber(null));
            player.setPartner(receivedPlayerName);
            sender.create("game", player);
            System.out.println("Sent!");
        }
    }

    private void joinGame(Player player, ArrayList<String> gameList) {
        if(gameList.size() == 0){
            System.out.println("There is not any created game");
            App.startGame();
        } else {
            System.out.println("--Created game names--");
            gameList.forEach(System.out::println);
            System.out.println();
            while (true) {
                System.out.print("Type selected game name: ");
                String partner = App.scanner.next();

                if (gameList.contains(partner)) {
                    player.setPartner(partner);
                    player.setAction(Action.JOIN);
                    sender.create("game", player);
                    break;
                }

                System.out.println("Not Found");
            }
        }
    }

    private boolean validateNumber(String value, Double received){
        try {
            double current = Double.parseDouble(value);

            if(null != received) {
                double sub = received - current;

                if (sub > 1 || sub < -1) {
                    return false;
                }

                return current % 3 == 0;
            } else {
                return true;
            }
        }catch (Exception ex){
            return false;
        }
    }

    private Double findNextNumber(Double receivedNumber) {
        if(null == receivedNumber){
            return new Random().nextInt(999) + 3d;
        }

        switch ((int) (receivedNumber % 3)){
            case 1: return receivedNumber - 1;
            case 2: return receivedNumber + 1;
            default: return receivedNumber;
        }
    }

}
