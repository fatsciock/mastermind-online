package it.negri.mastermind.server.players;

import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.model.Player;
import it.negri.mastermind.server.players.impl.PlayerApiImpl;

import java.util.concurrent.CompletableFuture;

public interface PlayerApi {
    CompletableFuture<Player> createPlayer(final String nick);

    CompletableFuture<Void> deletePlayer(final String nick);

    CompletableFuture<Player> getPlayer(final String nick);

    static PlayerApi of(Mastermind storage){
        return new PlayerApiImpl(storage);
    }
}
