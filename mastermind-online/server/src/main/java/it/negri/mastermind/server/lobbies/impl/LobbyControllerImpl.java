package it.negri.mastermind.server.lobbies.impl;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import it.negri.mastermind.server.AbstractController;
import it.negri.mastermind.server.lobbies.LobbyApi;
import it.negri.mastermind.server.lobbies.LobbyController;

public class LobbyControllerImpl extends AbstractController implements LobbyController {
    public LobbyControllerImpl(String path) {
        super(path);
    }

    private LobbyApi getApi(Context context) {
        return LobbyApi.of(getMastermindInstance(context));
    }

    @Override
    public void registerRoutes(Javalin app) {

    }

    @Override
    public void POSTCreateLobby(Context context) throws HttpResponseException {

    }

    @Override
    public void DeleteLobby(Context context) throws HttpResponseException {

    }

    @Override
    public void GETLobby(Context context) throws HttpResponseException {

    }

    @Override
    public void GETAllLobbies(Context context) throws HttpResponseException {

    }

    @Override
    public void PUTAddPlayerToLobby(Context context) throws HttpResponseException {

    }

    @Override
    public void DELETEPlayerFromLobby(Context context) throws HttpResponseException {

    }
}
