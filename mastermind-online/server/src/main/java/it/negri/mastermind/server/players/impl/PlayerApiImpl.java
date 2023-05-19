package it.negri.mastermind.server.players.impl;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.ConflictResponse;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.ServiceUnavailableResponse;
import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
import it.negri.mastermind.common.model.Player;
import it.negri.mastermind.server.AbstractApi;
import it.negri.mastermind.server.players.PlayerApi;

import java.util.concurrent.CompletableFuture;

public class PlayerApiImpl extends AbstractApi implements PlayerApi {
    public PlayerApiImpl(Mastermind storage) {
        super(storage);
    }

    @Override
    public CompletableFuture<Player> createPlayer(String nick) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return getStorage().createPlayer(nick);
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
    public CompletableFuture<Void> deletePlayer(String nick) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        getStorage().deletePlayer(nick);
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
    public CompletableFuture<Player> getPlayer(String nick) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return getStorage().getPlayer(nick);
                    } catch (MissingException e) {
                        throw new NotFoundResponse(e.getMessage());
                    } catch (ServerUnavailableException e) {
                        throw new ServiceUnavailableResponse(e.getMessage());
                    }
                }
        );
    }
}
