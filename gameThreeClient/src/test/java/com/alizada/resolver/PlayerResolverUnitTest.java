package com.alizada.resolver;

import com.alizada.App;
import com.alizada.model.Action;
import com.alizada.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerResolverUnitTest {
    private SenderMock senderMock = mock(SenderMock.class);

    private PlayerResolver playerResolver = new PlayerResolver(senderMock);
    ArgumentCaptor<Player> playerArgumentCaptor = ArgumentCaptor.forClass(Player.class);

    @BeforeEach
    void setUp(){
        doNothing().when(senderMock).create(anyString(),any());
    }

    @Test
    void shouldGenerateRightNumberForAutoPlaying(){
        App.autoGame = true;

        playerResolver.resolve(mockPlayer("player1", Action.PLAYING, 47d, "player2"),
                mockPlayer("player2", Action.PLAYING, null, "player1"));

        verify(senderMock).create(anyString(),playerArgumentCaptor.capture());

        assertTrue(playerArgumentCaptor.getValue().getValue().equals(48d/3d));
    }

    private Player mockPlayer(String name, Action action, Double value, String partner){
        Player player = new Player();
        player.setAction(action);
        player.setValue(value);
        player.setName(name);
        player.setPartner(partner);

        return player;
    }
}
