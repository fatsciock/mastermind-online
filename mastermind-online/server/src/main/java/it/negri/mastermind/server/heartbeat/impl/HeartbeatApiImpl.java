package it.negri.mastermind.server.heartbeat.impl;

import io.javalin.http.NotFoundResponse;
import io.javalin.http.ServiceUnavailableResponse;
import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
import it.negri.mastermind.server.AbstractApi;
import it.negri.mastermind.server.heartbeat.HeartbeatApi;

import java.util.concurrent.CompletableFuture;

public class HeartbeatApiImpl extends AbstractApi implements HeartbeatApi {
    public HeartbeatApiImpl(Mastermind storage) {
        super(storage);
    }

    @Override
    public CompletableFuture<Void> heartbeat(String nick) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        getStorage().heartbeat(nick);
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
