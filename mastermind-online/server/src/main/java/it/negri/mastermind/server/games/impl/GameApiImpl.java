package it.negri.mastermind.server.games.impl;

import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.model.Game;
import it.negri.mastermind.server.AbstractApi;
import it.negri.mastermind.server.games.GameApi;

import java.util.concurrent.CompletableFuture;

public class GameApiImpl extends AbstractApi implements GameApi {
    public GameApiImpl(Mastermind storage) {
        super(storage);
    }

    @Override
    public CompletableFuture<Game> startGame(int lobbyId) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteGame(int gameId) {
        return null;
    }

    @Override
    public CompletableFuture<Game> getGame(int gameId) {
        return null;
    }

    @Override
    public CompletableFuture<String> setCode(int gameId, String codeToGuess, String nick) {
        return null;
    }

    @Override
    public CompletableFuture<Game> guessCode(int gameId, String guess, String nick) {
        return null;
    }
}
