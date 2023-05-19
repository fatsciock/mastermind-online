package it.negri.mastermind.lobbies;

import it.negri.mastermind.client.MastermindClient;
import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
import it.negri.mastermind.server.MastermindService;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestRemoteLobbies extends AbstractTestLobbies {
    private static final int port = 10000;
    private MastermindService service;

    @BeforeAll
    @Override
    protected void setUp() {
        service = new MastermindService(port);
        service.start();
        mastermind = new MastermindClient("localhost", port);

        try {
            encoder = mastermind.createPlayer("Andrea");
            decoder = mastermind.createPlayer("Giorgio");
        } catch (ConflictException | IllegalArgumentException | ServerUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(1)
    @Test
    @Override
    protected void testCreateLobby() throws ServerUnavailableException {
        super.testCreateLobby();
    }

    @Order(2)
    @Test
    @Override
    protected void testDeleteLobby() throws MissingException, ServerUnavailableException {
        super.testDeleteLobby();
    }

    @Order(3)
    @Test
    @Override
    protected void testGetLobby() throws MissingException, ServerUnavailableException {
        super.testGetLobby();
    }

    @Order(4)
    @Test
    @Override
    protected void getAllLobbies() throws ServerUnavailableException {
        super.getAllLobbies();
    }

    @Order(5)
    @Test
    @Override
    protected void testAddPlayerToLobby() throws MissingException, ServerUnavailableException {
        super.testAddPlayerToLobby();
    }

    @Order(6)
    @Test
    @Override
    protected void testDeletePlayerFromLobby() throws MissingException, ServerUnavailableException {
        super.testDeletePlayerFromLobby();
        service.stop();
    }
}
