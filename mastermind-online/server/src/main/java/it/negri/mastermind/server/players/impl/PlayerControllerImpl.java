package it.negri.mastermind.server.players.impl;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import it.negri.mastermind.server.AbstractController;
import it.negri.mastermind.server.players.PlayerApi;
import it.negri.mastermind.server.players.PlayerController;

public class PlayerControllerImpl extends AbstractController implements PlayerController {
    public PlayerControllerImpl(String path) {
        super(path);
    }

    private PlayerApi getApi(Context context) {
        return PlayerApi.of(getMastermindInstance(context));
    }

    @Override
    public void registerRoutes(Javalin app) {

    }

    @Override
    public void POSTCreatePlayer(Context context) throws HttpResponseException {

    }

    @Override
    public void DELETEPlayer(Context context) throws HttpResponseException {

    }

    @Override
    public void GETPlayer(Context context) throws HttpResponseException {

    }
}
