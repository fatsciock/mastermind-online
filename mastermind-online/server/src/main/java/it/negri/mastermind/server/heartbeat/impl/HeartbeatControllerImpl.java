package it.negri.mastermind.server.heartbeat.impl;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import it.negri.mastermind.server.AbstractController;
import it.negri.mastermind.server.heartbeat.HeartbeatApi;
import it.negri.mastermind.server.heartbeat.HeartbeatController;
import it.negri.mastermind.server.utils.Filters;

public class HeartbeatControllerImpl extends AbstractController implements HeartbeatController {
    public HeartbeatControllerImpl(String path) {
        super(path);
    }

    private HeartbeatApi getApi(Context context) {
        return HeartbeatApi.of(getMastermindInstance(context));
    }

    @Override
    public void registerRoutes(Javalin app) {
        app.before(path("*"), Filters.ensureClientAcceptsMimeType("application", "json"));
        app.post(path("/"), this::POSTHeartbeat);
    }

    @Override
    public void POSTHeartbeat(Context context) throws HttpResponseException {
        HeartbeatApi api = getApi(context);
        var futureResult = api.heartbeat(context.bodyAsClass(String.class));
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }
}
