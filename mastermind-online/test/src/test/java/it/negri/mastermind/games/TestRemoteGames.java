package it.negri.mastermind.games;

import it.negri.mastermind.client.MastermindClient;
import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
import it.negri.mastermind.common.model.Role;
import it.negri.mastermind.server.MastermindService;
import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRemoteGames extends AbstractTestGames {
    private static final int port = 10000;
    private MastermindService service;

    @BeforeAll
    @Override
    protected void setUp() {
        service = new MastermindService(port);
        service.start();
        mastermind = new MastermindClient("localhost", port);

        try {
            mastermind.createLobby();
            encoder = mastermind.createPlayer("Andrea");
            encoder.setAsEncoder();
            decoder = mastermind.createPlayer("Giorgio");
            decoder.setAsDecoder();
            mastermind.addPlayerToLobby(encoder.getNickname(), 1, Role.ENCODER);
            lobby = mastermind.addPlayerToLobby(decoder.getNickname(), 1, Role.DECODER);
        } catch (ConflictException | MissingException | IllegalArgumentException | ServerUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(1)
    @Test
    @Override
    protected void testStartGame() throws MissingException, ConflictException, IllegalArgumentException, ServerUnavailableException {
        super.testStartGame();
    }

    @Order(2)
    @Test
    @Override
    protected void testGetGame() throws MissingException, ServerUnavailableException {
        super.testGetGame();
    }

    @Order(3)
    @Test
    @Override
    protected void testSetCode() throws MissingException, IllegalArgumentException, ConflictException, ServerUnavailableException {
        super.testSetCode();
    }

    @Order(4)
    @Test
    @Override
    protected void testGuessCodeDecoderWins() throws MissingException, IllegalArgumentException, ConflictException, ServerUnavailableException {
        super.testGuessCodeDecoderWins();
    }

    @Order(5)
    @Test
    @Override
    protected void testDeleteGame() throws MissingException, ServerUnavailableException {
        super.testDeleteGame();
    }

    @Order(6)
    @Test
    @Override
    protected void testGuessCodeEncoderWins() throws MissingException, IllegalArgumentException, ConflictException, ServerUnavailableException {
        super.testGuessCodeEncoderWins();
        service.stop();
    }
}
