package it.negri.mastermind.server.games;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.openapi.*;
import it.negri.mastermind.common.model.Game;
import it.negri.mastermind.server.Controller;
import it.negri.mastermind.server.MastermindService;
import it.negri.mastermind.server.games.impl.GameControllerImpl;

public interface GameController extends Controller {

    @OpenApi(
            operationId = "GameApi::startGame",
            path = MastermindService.BASE_URL + "/games/{id}",
            methods = {HttpMethod.POST},
            tags = {"games"},
            description = "Start a new game",
            pathParams = {
                    @OpenApiParam(
                            name = "id",
                            description = "The lobby id",
                            type = Integer.class,
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "201",
                            description = "Game started successfully",
                            content = {
                                    @OpenApiContent(
                                            from = Game.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: lobby is not full"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: no match with given id"
                    ),
                    @OpenApiResponse(
                            status = "409",
                            description = "Conflict: match with given id already started"
                    ),
                    @OpenApiResponse(
                            status = "503",
                            description = "Server offline: server currently unavailable"
                    )
            }
    )
    void POSTStartGame(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "GameApi::deleteGame",
            path = MastermindService.BASE_URL + "/games/{id}",
            methods = {HttpMethod.DELETE},
            tags = {"games"},
            description = "Delete a game, given its id",
            pathParams = {
                    @OpenApiParam(
                            name = "id",
                            type = Integer.class,
                            description = "The game id",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The provided id corresponds to a game, which is thus removed. Nothing is returned"
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided id corresponds to no known games"
                    ),
                    @OpenApiResponse(
                            status = "503",
                            description = "Server offline: server currently unavailable"
                    )
            }
    )
    void DELETEGame(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "GameApi::getGame",
            path = MastermindService.BASE_URL + "/games/{id}",
            methods = {HttpMethod.GET},
            tags = {"games"},
            description = "Gets the data of a game, given its id",
            pathParams = {
                    @OpenApiParam(
                            name = "id",
                            type = Integer.class,
                            description = "The game id whose data is being requested",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The provided identifier corresponds to a game, whose data is thus returned",
                            content = {
                                    @OpenApiContent(
                                            from = Game.class,
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
                            description = "Not found: the provided id corresponds to no known game"
                    ),
                    @OpenApiResponse(
                            status = "503",
                            description = "Server offline: server currently unavailable"
                    )
            }
    )
    void GETGame(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "GameApi::setCode",
            path = MastermindService.BASE_URL + "/games/{id}/{name}",
            methods = {HttpMethod.PUT},
            tags = {"games"},
            description = "Set the code to guess, given the game's id",
            pathParams = {
                    @OpenApiParam(
                            name = "id",
                            type = Integer.class,
                            description = "The game id",
                            required = true
                    ),
                    @OpenApiParam(
                            name = "name",
                            description = "The nickname of the player that set the code",
                            required = true
                    )
            },
            requestBody = @OpenApiRequestBody(
                    description = "The code to guess",
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
                            description = "The provided id corresponds to a game and the name to a player in the game, the code is set and returned"
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: the code is not valid (it's too short or too long or not contains only digit)"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided id corresponds to no known games"
                    )
            }
    )
    void PUTSetCode(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "GameApi::guessCode",
            path = MastermindService.BASE_URL + "/games/attempt/{id}/{name}",
            methods = {HttpMethod.PUT},
            tags = {"games"},
            description = "Try to guess the code, given the game's id",
            pathParams = {
                    @OpenApiParam(
                            name = "id",
                            type = Integer.class,
                            description = "The game id",
                            required = true
                    ),
                    @OpenApiParam(
                            name = "name",
                            description = "The nickname of the player that try to guess the code",
                            required = true
                    )
            },
            requestBody = @OpenApiRequestBody(
                    description = "The guess",
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
                            description = "The provided id corresponds to a game and the name to a player in the game, the attempt was made and the game is returned with the hint",
                            content = {
                                    @OpenApiContent(
                                            from = Game.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: the code is not valid (it's too short or too long or not contains only digit)"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided id corresponds to no known games"
                    )
            }
    )
    void PUTGuessCode(Context context) throws HttpResponseException;

    static GameController of(String root) {
        return new GameControllerImpl(root);
    }
}
