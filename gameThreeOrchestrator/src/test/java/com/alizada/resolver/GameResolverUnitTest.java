package com.alizada.resolver;

import com.alizada.App;
import com.alizada.model.Action;
import com.alizada.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameResolverUnitTest {
    private SenderMock sender = mock(SenderMock.class);

    private ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);
    private GameResolver gameResolver = new GameResolver(sender);

    @BeforeEach
    void setUp(){
        doNothing().when(sender).create(anyString(), any());
    }

    @Test
    public void shouldSendRightDataToPartnerForFinishGame(){
        gameResolver.resolve(mockPlayer("player1",Action.PLAYING, 1d, "player2"));

        verify(sender, times(2)).create(any(), playerArgumentCaptor.capture());
        assertEquals(playerArgumentCaptor.getAllValues().get(0).getAction(), Action.FINISH);
        assertEquals(playerArgumentCaptor.getAllValues().get(1).getAction(), Action.FINISH);
    }

    @Test
    public void shouldGetAvailableGameList(){
        App.playersMap = mockMap();
        ArrayList<String> availableGameList = new ArrayList<>();
        availableGameList.add("player3");
        availableGameList.add("player4");

        gameResolver.resolve(mockPlayer("player1", Action.SEARCH, null, "player2"));

        verify(sender).create(any(), playerArgumentCaptor.capture());
        assertEquals(playerArgumentCaptor.getValue().getGames(), availableGameList);
    }

    @Test
    public void shouldUpdateMapForJoinPlayer(){
        App.playersMap = mockMap();
        gameResolver.resolve(mockPlayer("player4", Action.JOIN, null, "partner"));

        verify(sender, times(2)).create(any(), playerArgumentCaptor.capture());

        assertEquals(playerArgumentCaptor.getAllValues().get(0).getAction(), Action.JOINED);
        assertEquals(playerArgumentCaptor.getAllValues().get(1).getAction(), Action.JOINED);
        assertEquals(playerArgumentCaptor.getAllValues().get(0).getName(), "player4");
        assertEquals(playerArgumentCaptor.getAllValues().get(1).getName(), "player4");
        assertEquals(App.playersMap.get("partner"), "player4");
    }

    private Map<String, String> mockMap() {
        Map<String, String> mockMap = new HashMap<>();
        mockMap.put("player1", "player2");
        mockMap.put("player3", "");
        mockMap.put("player4", "");
        return mockMap;
    }

    private Player mockPlayer(String name, Action action, Double value ,String partner){
        Player player = new Player();
        player.setAction(action);
        player.setValue(value);
        player.setName(name);
        player.setPartner(partner);

        return player;
    }

}
