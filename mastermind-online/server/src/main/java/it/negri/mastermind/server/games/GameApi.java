package it.negri.mastermind.server.games;

import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.model.Game;
import it.negri.mastermind.server.games.impl.GameApiImpl;

import java.util.concurrent.CompletableFuture;

public interface GameApi {
    CompletableFuture<Game> startGame(int lobbyId);
    CompletableFuture<Void> deleteGame(int gameId);
    CompletableFuture<Game> getGame(int gameId);
    CompletableFuture<String> setCode(int gameId, String codeToGuess, String nick);
    CompletableFuture<Game> guessCode(int gameId, String guess, String nick);

    static GameApi of(Mastermind storage) {
        return new GameApiImpl(storage);
    }
}
