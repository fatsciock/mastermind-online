package it.negri.mastermind.server.heartbeat;

import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.server.heartbeat.impl.HeartbeatApiImpl;

import java.util.concurrent.CompletableFuture;

public interface HeartbeatApi {
    CompletableFuture<Void> heartbeat(final String nick);

    static HeartbeatApi of(Mastermind storage) {
        return new HeartbeatApiImpl(storage);
    }
}
