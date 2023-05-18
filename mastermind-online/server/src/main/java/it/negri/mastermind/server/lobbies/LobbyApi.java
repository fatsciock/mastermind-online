package it.negri.mastermind.server.lobbies;

import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.model.Lobby;
import it.negri.mastermind.common.model.Role;
import it.negri.mastermind.server.lobbies.impl.LobbyApiImpl;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface LobbyApi {
    CompletableFuture<Lobby> createLobby();

    CompletableFuture<Void> deleteLobby(int lobbyId);

    CompletableFuture<Lobby> getLobby(final int lobbyId);

    CompletableFuture<Set<? extends Lobby>> getAllLobbies();

    CompletableFuture<Lobby> addPlayerToLobby(final String nick, final int lobbyId, final Role role);

    CompletableFuture<Void> deletePlayerFromLobby(final String nick, final int lobbyId);

    static LobbyApi of(Mastermind storage){
        return new LobbyApiImpl(storage);
    }
}
