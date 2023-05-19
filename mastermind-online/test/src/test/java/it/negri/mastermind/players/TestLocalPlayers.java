package it.negri.mastermind.players;

import it.negri.mastermind.common.LocalMastermind;
import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestLocalPlayers extends AbstractTestPlayers {

    @BeforeAll
    @Override
    protected void setUp() {
        mastermind = new LocalMastermind();
    }

    @Order(1)
    @Test
    @Override
    public void testCreatePlayer() throws ConflictException, IllegalArgumentException, ServerUnavailableException {
        super.testCreatePlayer();
    }

    @Order(3)
    @Test
    @Override
    public void testDeletePlayer() throws MissingException, ConflictException, ServerUnavailableException {
        super.testDeletePlayer();
    }

    @Order(2)
    @Test
    @Override
    public void testGetPlayer() throws MissingException, ConflictException, ServerUnavailableException {
        super.testGetPlayer();
    }
}
