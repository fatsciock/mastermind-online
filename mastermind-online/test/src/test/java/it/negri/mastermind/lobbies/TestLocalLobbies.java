package it.negri.mastermind.lobbies;

import it.negri.mastermind.common.LocalMastermind;
import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestLocalLobbies extends AbstractTestLobbies {
    @BeforeAll
    @Override
    protected void setUp() {
        this.mastermind = new LocalMastermind();

        try {
            this.encoder = this.mastermind.createPlayer("Andrea");
            this.decoder = this.mastermind.createPlayer("Giorgio");
        } catch (ConflictException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Order(1)
    @Test
    @Override
    protected void testCreateLobby() {
        super.testCreateLobby();
    }

    @Order(2)
    @Test
    @Override
    protected void testDeleteLobby() throws MissingException {
        super.testDeleteLobby();
    }

    @Order(3)
    @Test
    @Override
    protected void testGetLobby() throws MissingException {
        super.testGetLobby();
    }

    @Order(4)
    @Test
    @Override
    protected void getAllLobbies() {
        super.getAllLobbies();
    }

    @Order(5)
    @Test
    @Override
    protected void testAddPlayerToLobby() throws MissingException {
        super.testAddPlayerToLobby();
    }

    @Order(6)
    @Test
    @Override
    protected void testDeletePlayerFromLobby() throws MissingException {
        super.testDeletePlayerFromLobby();
    }
}
