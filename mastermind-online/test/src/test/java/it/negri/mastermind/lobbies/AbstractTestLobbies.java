package it.negri.mastermind.lobbies;

import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.model.Lobby;
import it.negri.mastermind.common.model.Player;
import it.negri.mastermind.common.model.Role;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractTestLobbies {

    protected Mastermind mastermind;

    protected Lobby lobby;

    protected Player encoder;

    protected Player decoder;

    protected abstract void setUp();

    protected void testCreateLobby() {
        Lobby expected = new Lobby(1);
        lobby = mastermind.createLobby();

        assertEquals(expected, lobby);

        expected = new Lobby(2);

        assertEquals(expected, mastermind.createLobby());
    }

    protected void testDeleteLobby() throws MissingException {
        mastermind.deleteLobby(2);
        assertThrows(MissingException.class, () -> mastermind.getLobby(2));
    }

    protected void testGetLobby() throws MissingException {
        assertEquals(lobby, mastermind.getLobby(1));
        assertThrows(MissingException.class, () -> mastermind.getLobby(2));
    }

    protected void getAllLobbies() {
        assertEquals(1, mastermind.getAllLobbies().size());
        mastermind.createLobby();
        assertEquals(2, mastermind.getAllLobbies().size());
        assertTrue(mastermind.getAllLobbies().contains(lobby));
    }

    protected void testAddPlayerToLobby() throws MissingException {
        encoder.setAsEncoder();
        assertEquals(encoder, mastermind.addPlayerToLobby(encoder.getNickname(), 1, Role.ENCODER).getPlayerA());
        //Il player non può essere inattivo
        assertThrows(IllegalArgumentException.class, () -> mastermind.addPlayerToLobby(decoder.getNickname(), 1, Role.INACTIVE));
        //Il player non può ricoprire un ruolo già scelto
        assertThrows(IllegalArgumentException.class, () -> mastermind.addPlayerToLobby(decoder.getNickname(), 1, Role.ENCODER));
        decoder.setAsDecoder();
        assertEquals(decoder, mastermind.addPlayerToLobby(decoder.getNickname(), 1, Role.DECODER).getPlayerB());
        //La lobby è piena
        assertThrows(IllegalArgumentException.class, () -> mastermind.addPlayerToLobby(encoder.getNickname(), 1, Role.ENCODER));
    }

    protected void testDeletePlayerFromLobby() throws MissingException {
        mastermind.deletePlayerFromLobby(decoder.getNickname(), 1);
        assertNull(mastermind.getLobby(1).getPlayerB());
        assertThrows(MissingException.class, () -> mastermind.deletePlayerFromLobby(decoder.getNickname(), 1));
        mastermind.deletePlayerFromLobby(encoder.getNickname(), 1);
        assertThrows(MissingException.class, () -> mastermind.getLobby(1));
    }
}
