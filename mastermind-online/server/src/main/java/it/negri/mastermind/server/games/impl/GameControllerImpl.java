package it.negri.mastermind.server.games.impl;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import it.negri.mastermind.server.AbstractController;
import it.negri.mastermind.server.games.GameApi;
import it.negri.mastermind.server.games.GameController;

public class GameControllerImpl extends AbstractController implements GameController {
    public GameControllerImpl(String path) {
        super(path);
    }

    private GameApi getApi(Context context) {
        return GameApi.of(getMastermindInstance(context));
    }

    @Override
    public void registerRoutes(Javalin app) {

    }

    @Override
    public void POSTStartGame(Context context) throws HttpResponseException {

    }

    @Override
    public void DELETEGame(Context context) throws HttpResponseException {

    }

    @Override
    public void GETGame(Context context) throws HttpResponseException {

    }

    @Override
    public void PUTSetCode(Context context) throws HttpResponseException {

    }

    @Override
    public void PUTGuessCode(Context context) throws HttpResponseException {

    }
}
