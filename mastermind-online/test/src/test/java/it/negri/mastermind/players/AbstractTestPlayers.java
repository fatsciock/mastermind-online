package it.negri.mastermind.players;

import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
import it.negri.mastermind.common.model.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractTestPlayers {

    protected Mastermind mastermind;
    protected Player player1;

    protected abstract void setUp();

    protected void testCreatePlayer() throws ConflictException, IllegalArgumentException, ServerUnavailableException {
        String nickname = "Andrea";
        Player expected = new Player(nickname);
        player1 = mastermind.createPlayer(nickname);

        assertEquals(expected, player1);
        assertThrows(ConflictException.class, () -> mastermind.createPlayer(nickname));
        assertThrows(IllegalArgumentException.class, () -> mastermind.createPlayer(""));
    }

    protected void testDeletePlayer() throws MissingException, ConflictException, ServerUnavailableException {
        mastermind.deletePlayer(player1.getNickname());
        assertThrows(MissingException.class, () -> mastermind.getPlayer(player1.getNickname()));
    }

    protected void testGetPlayer() throws MissingException, ConflictException, ServerUnavailableException {
        assertEquals(player1, mastermind.getPlayer(player1.getNickname()));
        assertThrows(MissingException.class, () -> mastermind.getPlayer("Carlo"));
    }
}
