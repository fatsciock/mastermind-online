package it.negri.mastermind.server.games.impl;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.ConflictResponse;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.ServiceUnavailableResponse;
import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
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
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return getStorage().startGame(lobbyId);
                    } catch (MissingException e) {
                        throw new NotFoundResponse(e.getMessage());
                    } catch (ConflictException e) {
                        throw new ConflictResponse(e.getMessage());
                    } catch (IllegalArgumentException e) {
                        throw new BadRequestResponse(e.getMessage());
                    } catch (ServerUnavailableException e) {
                        throw new ServiceUnavailableResponse(e.getMessage());
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Void> deleteGame(int gameId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        getStorage().deleteGame(gameId);
                        return null;
                    } catch (MissingException e) {
                        throw new NotFoundResponse(e.getMessage());
                    } catch (ServerUnavailableException e) {
                        throw new ServiceUnavailableResponse(e.getMessage());
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Game> getGame(int gameId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return getStorage().getGame(gameId);
                    } catch (MissingException e) {
                        throw new NotFoundResponse(e.getMessage());
                    } catch (ServerUnavailableException e) {
                        throw new ServiceUnavailableResponse(e.getMessage());
                    }
                }
        );
    }

    @Override
    public CompletableFuture<String> setCode(int gameId, String codeToGuess, String nick) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return getStorage().setCode(gameId, codeToGuess, nick);
                    } catch (MissingException e) {
                        throw new NotFoundResponse(e.getMessage());
                    } catch (IllegalArgumentException e) {
                        throw new BadRequestResponse(e.getMessage());
                    } catch (ServerUnavailableException e) {
                        throw new ServiceUnavailableResponse(e.getMessage());
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Game> guessCode(int gameId, String guess, String nick) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return getStorage().guessCode(gameId, guess, nick);
                    } catch (MissingException e) {
                        throw new NotFoundResponse(e.getMessage());
                    } catch (IllegalArgumentException e) {
                        throw new BadRequestResponse(e.getMessage());
                    } catch (ServerUnavailableException e) {
                        throw new ServiceUnavailableResponse(e.getMessage());
                    }
                }
        );
    }
}
