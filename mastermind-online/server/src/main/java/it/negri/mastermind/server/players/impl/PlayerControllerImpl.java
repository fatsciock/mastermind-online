package it.negri.mastermind.server.players.impl;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import it.negri.mastermind.server.AbstractController;
import it.negri.mastermind.server.players.PlayerApi;
import it.negri.mastermind.server.players.PlayerController;
import it.negri.mastermind.server.utils.Filters;

public class PlayerControllerImpl extends AbstractController implements PlayerController {
    public PlayerControllerImpl(String path) {
        super(path);
    }

    private PlayerApi getApi(Context context) {
        return PlayerApi.of(getMastermindInstance(context));
    }

    @Override
    public void registerRoutes(Javalin app) {
        app.before(path("*"), Filters.ensureClientAcceptsMimeType("application", "json"));
        app.post(path("/{name}"), this::POSTCreatePlayer);
        app.get(path("/{name}"), this::GETPlayer);
        app.delete(path("/{name}"), this::DELETEPlayer);
    }

    @Override
    public void POSTCreatePlayer(Context context) throws HttpResponseException {
        PlayerApi api = getApi(context);
        var futureResult = api.createPlayer(context.pathParam("{name}"));
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void DELETEPlayer(Context context) throws HttpResponseException {
        PlayerApi api = getApi(context);
        var futureResult = api.deletePlayer(context.pathParam("{name}"));
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void GETPlayer(Context context) throws HttpResponseException {
        PlayerApi api = getApi(context);
        var futureResult = api.getPlayer(context.pathParam("{name}"));
        asyncReplyWithBody(context, "application/json", futureResult);
    }
}
