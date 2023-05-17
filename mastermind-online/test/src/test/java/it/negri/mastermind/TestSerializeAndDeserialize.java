package it.negri.mastermind;

import com.google.gson.Gson;
import it.negri.mastermind.common.model.Game;
import it.negri.mastermind.common.model.Lobby;
import it.negri.mastermind.common.model.Player;
import it.negri.mastermind.common.utils.GsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSerializeAndDeserialize {

    private final Player ANDREA = new Player("Andrea");
    private final Player GIORGIO = new Player("Giorgio");

    private final Lobby LOBBY_FULL = new Lobby(1);
    private final Lobby LOBBY_EMPTY = new Lobby(2);

    private final Game GAME = new Game(LOBBY_FULL);

    private Gson gson;

    @BeforeEach
    public void setup() {
        gson = GsonUtils.createGson();
        ANDREA.setAsEncoder();
        GIORGIO.setAsDecoder();
        LOBBY_FULL.setPlayerA(ANDREA);
        LOBBY_FULL.setPlayerB(GIORGIO);
    }

    @Test
    public void testPlayer() {
        for (var player : List.of(ANDREA, GIORGIO)) {
            String serialized = gson.toJson(player);
            Player deserialized = gson.fromJson(serialized, Player.class);
            assertEquals(player, deserialized);
        }
    }

    @Test
    public void testLobby() {
        for (var lobby : List.of(LOBBY_FULL, LOBBY_EMPTY)) {
            String serialized = gson.toJson(lobby);
            Lobby deserialized = gson.fromJson(serialized, Lobby.class);
            assertEquals(lobby, deserialized);
        }
    }

    @Test
    public void testGame() {
        String serialized = gson.toJson(GAME);
        Game deserialized = gson.fromJson(serialized, Game.class);
        assertEquals(GAME, deserialized);
    }
}
