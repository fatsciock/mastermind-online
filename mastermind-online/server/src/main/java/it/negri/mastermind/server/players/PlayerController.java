package it.negri.mastermind.server.players;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.openapi.*;
import it.negri.mastermind.common.model.Player;
import it.negri.mastermind.server.Controller;
import it.negri.mastermind.server.MastermindService;
import it.negri.mastermind.server.players.impl.PlayerControllerImpl;

public interface PlayerController extends Controller {

    @OpenApi(
            operationId = "PlayerApi::createPlayer",
            path = MastermindService.BASE_URL + "/players/{name}",
            methods = {HttpMethod.POST},
            tags = {"players"},
            description = "Create a new player",
            pathParams = {
                    @OpenApiParam(
                            name = "name",
                            description = "The nickname of the player to create",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "201",
                            description = "Player created successfully",
                            content = {
                                    @OpenApiContent(
                                            from = Player.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: player with given nickname already exist"
                    ),
                    @OpenApiResponse(
                            status = "503",
                            description = "Server offline: server currently unavailable"
                    )
            }
    )
    void POSTCreatePlayer(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "PlayerApi::deletePlayer",
            path = MastermindService.BASE_URL + "/players/{name}",
            methods = {HttpMethod.DELETE},
            tags = {"players"},
            description = "Delete a player, given its nickname",
            pathParams = {
                    @OpenApiParam(
                            name = "name",
                            description = "The nickname of the player to delete",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "201",
                            description = "The provided nickname corresponds to a player, which is thus removed. Nothing is returned"
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
    void DELETEPlayer(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "PlayerApi::getPlayer",
            path = MastermindService.BASE_URL + "/players/{name}",
            methods = {HttpMethod.GET},
            tags = {"players"},
            description = "Gets the data of a player, given its nickname",
            pathParams = {
                    @OpenApiParam(
                            name = "name",
                            description = "The nickname of the user whose data is being requested",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The provided nickname corresponds to a player, whose data is thus returned",
                            content = {
                                    @OpenApiContent(
                                            from = Player.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided nickname corresponds to no known player"
                    ),
                    @OpenApiResponse(
                            status = "503",
                            description = "Server offline: server currently unavailable"
                    )
            }
    )
    void GETPlayer(Context context) throws HttpResponseException;

    static PlayerController of(String root) {
        return new PlayerControllerImpl(root);
    }
}
