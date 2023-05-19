package it.negri.mastermind.server.games.impl;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import it.negri.mastermind.server.AbstractController;
import it.negri.mastermind.server.games.GameApi;
import it.negri.mastermind.server.games.GameController;
import it.negri.mastermind.server.utils.Filters;

public class GameControllerImpl extends AbstractController implements GameController {
    public GameControllerImpl(String path) {
        super(path);
    }

    private GameApi getApi(Context context) {
        return GameApi.of(getMastermindInstance(context));
    }

    @Override
    public void registerRoutes(Javalin app) {
        app.before(path("*"), Filters.ensureClientAcceptsMimeType("application", "json"));
        app.post(path("/{id}"), this::POSTStartGame);
        app.delete(path("/{id}"), this::DELETEGame);
        app.get(path("/{id}"), this::GETGame);
        app.put(path("/{id}/{name}"), this::PUTSetCode);
        app.put(path("/attempt/{id}/{name}"), this::PUTGuessCode);
    }

    @Override
    public void POSTStartGame(Context context) throws HttpResponseException {
        GameApi api = getApi(context);
        var futureResult = api.startGame(Integer.parseInt(context.pathParam("{id}")));
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void DELETEGame(Context context) throws HttpResponseException {
        GameApi api = getApi(context);
        var futureResult = api.deleteGame(Integer.parseInt(context.pathParam("{id}")));
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void GETGame(Context context) throws HttpResponseException {
        GameApi api = getApi(context);
        var futureResult = api.getGame(Integer.parseInt(context.pathParam("{id}")));
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void PUTSetCode(Context context) throws HttpResponseException {
        GameApi api = getApi(context);
        var futureResult = api.setCode(Integer.parseInt(context.pathParam("{id}")),
                context.bodyAsClass(String.class),
                context.pathParam("{name}"));
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void PUTGuessCode(Context context) throws HttpResponseException {
        GameApi api = getApi(context);
        var futureResult = api.guessCode(Integer.parseInt(context.pathParam("{id}")),
                context.bodyAsClass(String.class),
                context.pathParam("{name}"));
        asyncReplyWithBody(context, "application/json", futureResult);
    }
}
