package it.negri.mastermind.games;

import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.model.Lobby;
import it.negri.mastermind.common.model.Game;
import it.negri.mastermind.common.model.Player;
import it.negri.mastermind.common.model.ResultLabel;
import it.negri.mastermind.common.utils.Utils;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractTestGames {
    protected Mastermind mastermind;
    protected Lobby lobby;
    protected Game game;
    protected Player encoder;
    protected Player decoder;
    protected abstract void setUp();

    protected void testStartGame() throws MissingException, ConflictException, IllegalArgumentException {
        Game expected = new Game(lobby);
        game = mastermind.startGame(lobby.getId());

        assertEquals(expected.getId(), game.getId());
        assertEquals(Utils.getDefaultAttempts(), game.getRemainingAttempts());
        assertTrue(encoder.equals(game.getPlayerA()) || encoder.equals(game.getPlayerB()));
        assertTrue(decoder.equals(game.getPlayerA()) || decoder.equals(game.getPlayerB()));

        assertThrows(MissingException.class, () -> mastermind.startGame(2));
        assertThrows(ConflictException.class, () -> mastermind.startGame(lobby.getId()));
        var lobbyTemp = mastermind.createLobby();
        assertThrows(IllegalArgumentException.class, () -> mastermind.startGame(lobbyTemp.getId()));
        mastermind.deleteLobby(lobbyTemp.getId());
    }

    protected void testDeleteGame() throws MissingException {
        mastermind.deleteGame(game.getId());
        assertThrows(MissingException.class, () -> mastermind.getGame(game.getId()));
    }

    protected void testGetGame() throws MissingException {
        assertEquals(game, mastermind.getGame(1));
        assertThrows(MissingException.class, () -> mastermind.getGame(2));
    }

    protected void testSetCode() throws MissingException, IllegalArgumentException, ConflictException {
        String codeToGuess = "0321";
        //Game non esistente
        assertThrows(MissingException.class, () -> mastermind.setCode(2, codeToGuess, encoder.getNickname()));
        //Player non esistente
        assertThrows(MissingException.class, () -> mastermind.setCode(game.getId(), codeToGuess, "Asdrubale"));
        var tempPlayer = mastermind.createPlayer("Temp");
        //Player non appartenente al Game
        assertThrows(IllegalArgumentException.class, () -> mastermind.setCode(game.getId(), codeToGuess, tempPlayer.getNickname()));
        mastermind.deletePlayer(tempPlayer.getNickname());
        //Il Decoder non può impostare il codice segreto
        assertThrows(IllegalArgumentException.class, () -> mastermind.setCode(game.getId(), codeToGuess, decoder.getNickname()));
        //Codice errato perché alfanumerico
        assertThrows(IllegalArgumentException.class, () -> mastermind.setCode(game.getId(), "AB24", encoder.getNickname()));

        assertEquals(codeToGuess, mastermind.setCode(game.getId(), codeToGuess, encoder.getNickname()));
    }

    protected void testGuessCodeDecoderWins() throws MissingException, IllegalArgumentException, ConflictException {
        String guess = "0321";
        //Game non esistente
        assertThrows(MissingException.class, () -> mastermind.guessCode(2, guess, decoder.getNickname()));
        //Player non esistente
        assertThrows(MissingException.class, () -> mastermind.guessCode(game.getId(), guess, "Asdrubale"));
        var tempPlayer = mastermind.createPlayer("Temp");
        //Player non appartenente al Game
        assertThrows(IllegalArgumentException.class, () -> mastermind.guessCode(game.getId(), guess, tempPlayer.getNickname()));
        mastermind.deletePlayer(tempPlayer.getNickname());
        //L'Encoder non può provare a indovinare il codice segreto
        assertThrows(IllegalArgumentException.class, () -> mastermind.guessCode(game.getId(), guess, encoder.getNickname()));
        //Codice errato perché più lungo de consentito
        assertThrows(IllegalArgumentException.class, () -> mastermind.guessCode(game.getId(), "456224", decoder.getNickname()));

        //Primo tentativo: 3 in posizione corretta, 1 in posizione errata
        game = mastermind.guessCode(game.getId(), "1315", decoder.getNickname());
        assertEquals(1, game.getHintPerAttempt().size());
        assertEquals(Utils.getDefaultAttempts() - 1, game.getRemainingAttempts());
        var lastHint = game.getHintPerAttempt().get(0);
        assertEquals(1, lastHint.get(ResultLabel.RIGHT_NUMBER_IN_WRONG_PLACE));
        assertEquals(1, lastHint.get(ResultLabel.RIGHT_NUMBER_IN_RIGHT_PLACE));

        //Secondo tentativo: codice esatto
        game = mastermind.guessCode(game.getId(), guess, decoder.getNickname());
        assertEquals(2, game.getHintPerAttempt().size());
        assertEquals(Utils.getDefaultAttempts() - 2, game.getRemainingAttempts());
        lastHint = game.getHintPerAttempt().get(1);
        assertEquals(0, lastHint.get(ResultLabel.RIGHT_NUMBER_IN_WRONG_PLACE));
        assertEquals(Utils.getCodeLength(), lastHint.get(ResultLabel.RIGHT_NUMBER_IN_RIGHT_PLACE));
        assertEquals(decoder, game.getWinner());
    }

    protected void testGuessCodeEncoderWins() throws MissingException, IllegalArgumentException, ConflictException {
        game = mastermind.startGame(lobby.getId());
        String codeToGuess = "0321";
        mastermind.setCode(game.getId(), codeToGuess, encoder.getNickname());

        for(int i=0; i<Utils.getDefaultAttempts(); i++) {
            game = mastermind.guessCode(game.getId(), "1315", decoder.getNickname());
        }

        assertEquals(Utils.getDefaultAttempts(), game.getHintPerAttempt().size());
        assertEquals(0, game.getRemainingAttempts());
        assertEquals(encoder, game.getWinner());
    }
}
