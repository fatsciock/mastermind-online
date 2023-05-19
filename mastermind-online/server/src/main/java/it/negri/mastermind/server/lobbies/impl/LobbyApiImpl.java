package it.negri.mastermind.server.lobbies.impl;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.ServiceUnavailableResponse;
import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
import it.negri.mastermind.common.model.Lobby;
import it.negri.mastermind.common.model.Role;
import it.negri.mastermind.server.AbstractApi;
import it.negri.mastermind.server.lobbies.LobbyApi;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LobbyApiImpl extends AbstractApi implements LobbyApi {
    public LobbyApiImpl(Mastermind storage) {
        super(storage);
    }

    @Override
    public CompletableFuture<Lobby> createLobby() {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return getStorage().createLobby();
                    } catch (ServerUnavailableException e) {
                        throw new ServiceUnavailableResponse(e.getMessage());
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Void> deleteLobby(int lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        getStorage().deleteLobby(lobbyId);
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
    public CompletableFuture<Lobby> getLobby(int lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return getStorage().getLobby(lobbyId);
                    } catch (MissingException e) {
                        throw new NotFoundResponse(e.getMessage());
                    } catch (ServerUnavailableException e) {
                        throw new ServiceUnavailableResponse(e.getMessage());
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Set<? extends Lobby>> getAllLobbies() {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return getStorage().getAllLobbies();
                    } catch (ServerUnavailableException e) {
                        throw new ServiceUnavailableResponse(e.getMessage());
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Lobby> addPlayerToLobby(String nick, int lobbyId, Role role) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return getStorage().addPlayerToLobby(nick, lobbyId, role);
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
    public CompletableFuture<Void> deletePlayerFromLobby(String nick, int lobbyId) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        getStorage().deletePlayerFromLobby(nick, lobbyId);
                        return null;
                    } catch (MissingException e) {
                        throw new NotFoundResponse(e.getMessage());
                    } catch (ServerUnavailableException e) {
                        throw new ServiceUnavailableResponse(e.getMessage());
                    }
                }
        );
    }
}
