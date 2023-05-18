package it.negri.mastermind.server.players.impl;

import it.negri.mastermind.common.Mastermind;
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
        return null;
    }

    @Override
    public CompletableFuture<Void> deletePlayer(String nick) {
        return null;
    }

    @Override
    public CompletableFuture<Player> getPlayer(String nick) {
        return null;
    }
}
