package it.negri.mastermind.server.heartbeat;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.openapi.*;
import it.negri.mastermind.server.Controller;
import it.negri.mastermind.server.MastermindService;
import it.negri.mastermind.server.heartbeat.impl.HeartbeatControllerImpl;

public interface HeartbeatController extends Controller {

    @OpenApi(
            operationId = "HeartbeatApi::heartbeat",
            path = MastermindService.BASE_URL + "/heartbeat",
            methods = {HttpMethod.POST},
            tags = {"heartbeat"},
            description = "Send an heartbeat to the server",
            requestBody = @OpenApiRequestBody(
                    description = "The nickname of the client that send the heartbeat",
                    required = true,
                    content = {
                            @OpenApiContent(
                                    from = String.class,
                                    mimeType = ContentType.JSON
                            )
                    }
            ),
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The provided nickname corresponds to a player. Nothing is returned"
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided nickname corresponds to no known user"
                    ),
                    @OpenApiResponse(
                            status = "503",
                            description = "Server offline: server currently unavailable"
                    )
            }
    )
    void POSTHeartbeat(Context context) throws HttpResponseException;

    static HeartbeatController of(String root) {
        return new HeartbeatControllerImpl(root);
    }
}
