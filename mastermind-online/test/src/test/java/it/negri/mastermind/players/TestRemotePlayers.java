package it.negri.mastermind.players;

import it.negri.mastermind.client.MastermindClient;
import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
import it.negri.mastermind.server.MastermindService;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestRemotePlayers extends AbstractTestPlayers {
    private static final int port = 10000;
    private MastermindService service;

    @BeforeAll
    @Override
    protected void setUp() {
        service = new MastermindService(port);
        service.start();
        mastermind = new MastermindClient("localhost", port);
    }

    @Order(1)
    @Test
    @Override
    protected void testCreatePlayer() throws ConflictException, IllegalArgumentException, ServerUnavailableException {
        super.testCreatePlayer();
    }

    @Order(3)
    @Test
    @Override
    protected void testDeletePlayer() throws MissingException, ConflictException, ServerUnavailableException {
        super.testDeletePlayer();
        service.stop();
    }

    @Order(2)
    @Test
    @Override
    protected void testGetPlayer() throws MissingException, ConflictException, ServerUnavailableException {
        super.testGetPlayer();
    }
}
