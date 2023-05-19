package it.negri.mastermind.server.lobbies.impl;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import it.negri.mastermind.common.model.Role;
import it.negri.mastermind.server.AbstractController;
import it.negri.mastermind.server.lobbies.LobbyApi;
import it.negri.mastermind.server.lobbies.LobbyController;
import it.negri.mastermind.server.utils.Filters;

public class LobbyControllerImpl extends AbstractController implements LobbyController {
    public LobbyControllerImpl(String path) {
        super(path);
    }

    private LobbyApi getApi(Context context) {
        return LobbyApi.of(getMastermindInstance(context));
    }

    @Override
    public void registerRoutes(Javalin app) {
        app.before(path("*"), Filters.ensureClientAcceptsMimeType("application", "json"));
        app.post(path("/"), this::POSTCreateLobby);
        app.delete(path("/{id}"), this::DELETELobby);
        app.get(path("/{id}"), this::GETLobby);
        app.get(path("/"), this::GETAllLobbies);
        app.put(path("/{id}/{name}/{role}"), this::PUTAddPlayerToLobby);
        app.delete(path("/{id}/{name}"), this::DELETEPlayerFromLobby);
    }

    @Override
    public void POSTCreateLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var futureResult = api.createLobby();
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void DELETELobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var futureResult = api.deleteLobby(Integer.parseInt(context.pathParam("{id}")));
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void GETLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var futureResult = api.getLobby(Integer.parseInt(context.pathParam("{id}")));
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void GETAllLobbies(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var futureResult = api.getAllLobbies();
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void PUTAddPlayerToLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var futureResult = api.addPlayerToLobby(context.pathParam("{name}"),
                Integer.parseInt(context.pathParam("{id}")),
                Role.valueOf(context.pathParam("{role}").toUpperCase()));
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void DELETEPlayerFromLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);
        var futureResult = api.deletePlayerFromLobby(context.pathParam("{name}"),
                Integer.parseInt(context.pathParam("{id}")));
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }
}
