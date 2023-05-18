package it.negri.mastermind.server.lobbies.impl;

import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.model.Lobby;
import it.negri.mastermind.common.model.Role;
import it.negri.mastermind.server.AbstractApi;
import it.negri.mastermind.server.lobbies.LobbyApi;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LobbyApiImpl extends AbstractApi implements LobbyApi {
    public LobbyApiImpl(Mastermind storage) {
        super(storage);
    }

    @Override
    public CompletableFuture<Lobby> createLobby() {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteLobby(int lobbyId) {
        return null;
    }

    @Override
    public CompletableFuture<Lobby> getLobby(int lobbyId) {
        return null;
    }

    @Override
    public CompletableFuture<Set<? extends Lobby>> getAllLobbies() {
        return null;
    }

    @Override
    public CompletableFuture<Lobby> addPlayerToLobby(String nick, int lobbyId, Role role) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deletePlayerFromLobby(String nick, int lobbyId) {
        return null;
    }
}
