package com.alizada.resolver;

import com.alizada.App;
import com.alizada.model.Action;
import com.alizada.model.Player;
import com.alizada.rabbitmq.Sender;

import java.util.ArrayList;

public class GameResolver {
    private Sender sender;

    public GameResolver(Sender sender) {
        this.sender = sender;
    }

    public void resolve(Player receivedPlayedData) {
        Action receivedAction = receivedPlayedData.getAction();

        switch (receivedAction){
            case CREATE: createGame(receivedPlayedData); break;
            case SEARCH: searchGame(receivedPlayedData); break;
            case JOIN: joinGame(receivedPlayedData.getPartner(), receivedPlayedData); break;
            case PLAYING: sendToPartner(receivedPlayedData); break;
        }
    }

    private void sendToPartner(Player receivedPlayedData) {
        double receivedNumber = receivedPlayedData.getValue();
        receivedPlayedData.setValue(receivedNumber);
        if(receivedNumber == 1d){
            receivedPlayedData.setAction(Action.FINISH);
            sender.create(receivedPlayedData.getName(), receivedPlayedData);
        }

        sender.create(receivedPlayedData.getPartner(), receivedPlayedData);
    }

    private void searchGame(Player player) {
        player.setGames(getAvailableGameList());
        player.setAction(Action.RESULT);

        sender.create(player.getName(), player);
    }

    private ArrayList<String> getAvailableGameList(){
        ArrayList<String> gameList = new ArrayList<>();
        App.playersMap.entrySet().stream()
                .filter(res -> res.getValue().equals("")).forEach(key -> gameList.add(key.getKey()));

        return gameList;
    }

    private void joinGame(String partner, Player player) {
        App.playersMap.put(partner, player.getName());
        player.setAction(Action.JOINED);
        sender.create(player.getName(), player);
        sender.create(partner, player);
    }

    private void createGame(Player player) {
        App.playersMap.put(player.getName(), "");
        player.setAction(Action.CREATED);
        sender.create(player.getName(), player);
}

}
