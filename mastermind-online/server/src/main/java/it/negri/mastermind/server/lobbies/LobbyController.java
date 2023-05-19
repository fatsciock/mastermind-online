package it.negri.mastermind.server.lobbies;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.openapi.*;
import it.negri.mastermind.common.model.Lobby;
import it.negri.mastermind.common.model.Player;
import it.negri.mastermind.server.Controller;
import it.negri.mastermind.server.MastermindService;
import it.negri.mastermind.server.lobbies.impl.LobbyControllerImpl;

public interface LobbyController extends Controller {

    @OpenApi(
            operationId = "LobbyApi::createLobby",
            path = MastermindService.BASE_URL + "/lobbies",
            methods = {HttpMethod.POST},
            tags = {"lobbies"},
            description = "Create a new lobby",
            responses = {
                    @OpenApiResponse(
                            status = "201",
                            description = "The newly created lobby",
                            content = {
                                    @OpenApiContent(
                                            from = Lobby.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request"
                    ),
                    @OpenApiResponse(
                            status = "503",
                            description = "Server offline: server currently unavailable"
                    )
            }
    )
    void POSTCreateLobby(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::deleteLobby",
            path = MastermindService.BASE_URL + "/lobbies/{id}",
            methods = {HttpMethod.DELETE},
            tags = {"lobbies"},
            description = "Delete a lobby, given its id",
            pathParams = {
                    @OpenApiParam(
                            name = "id",
                            type = Integer.class,
                            description = "The lobby id",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The provided id corresponds to a lobby, which is thus removed. Nothing is returned"
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided id corresponds to no known lobby"
                    ),
                    @OpenApiResponse(
                            status = "503",
                            description = "Server offline: server currently unavailable"
                    )
            }
    )
    void DeleteLobby(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::getLobby",
            path = MastermindService.BASE_URL + "/lobbies/{id}",
            methods = {HttpMethod.GET},
            tags = {"lobbies"},
            description = "Gets the data of a lobby, given its id",
            pathParams = {
                    @OpenApiParam(
                            name = "id",
                            type = Integer.class,
                            description = "The lobby id",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The provided id corresponds to a lobby, whose data is thus returned",
                            content = {
                                    @OpenApiContent(
                                            from = Lobby.class,
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
                            description = "Not found: the provided id corresponds to no known lobby"
                    ),
                    @OpenApiResponse(
                            status = "503",
                            description = "Server offline: server currently unavailable"
                    )
            }
    )
    void GETLobby(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::getAllLobbiesId",
            path = MastermindService.BASE_URL + "/lobbies",
            methods = {HttpMethod.GET},
            tags = {"lobbies"},
            description = "Retrieves all lobbies",
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "A Set containing all the lobbies",
                            content = {
                                    @OpenApiContent(
                                            from = Lobby[].class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request"
                    ),
                    @OpenApiResponse(
                            status = "503",
                            description = "Server offline: server currently unavailable"
                    )
            }
    )
    void GETAllLobbies(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::addPlayerToLobby",
            path = MastermindService.BASE_URL + "/lobbies/{id}/{name}/{role}",
            methods = {HttpMethod.PUT},
            tags = {"lobbies"},
            description = "Add the player corresponding to the nickname passed, in the lobby corresponding to the id passed.",
            pathParams = {
                    @OpenApiParam(
                            name = "id",
                            type = Integer.class,
                            description = "The lobby id",
                            required = true
                    ),
                    @OpenApiParam(
                            name = "name",
                            description = "The nickname of the player to add",
                            required = true
                    ),
                    @OpenApiParam(
                            name = "role",
                            description = "The role of the player",
                            required = true
                    )
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "The provided id corresponds to a lobby, the player is connected to the lobby that is returned",
                            content = {
                                    @OpenApiContent(
                                            from = Lobby.class,
                                            mimeType = ContentType.JSON
                                    )
                            }
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request: player must chose a different role or lobby is full "
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided id corresponds to not existing lobby or not existing player"
                    ),
                    @OpenApiResponse(
                            status = "503",
                            description = "Server offline: server currently unavailable"
                    )
            }
    )
    void PUTAddPlayerToLobby(Context context) throws HttpResponseException;

    @OpenApi(
            operationId = "LobbyApi::deletePlayerFromLobby",
            path = MastermindService.BASE_URL + "/lobbies/{id}/{name}",
            methods = {HttpMethod.DELETE},
            tags = {"lobbies"},
            description = "Remove a player from a lobby, given the id of the lobby and the nickname",
            pathParams = {
                    @OpenApiParam(
                            name = "id",
                            type = Integer.class,
                            description = "The lobby id",
                            required = true
                    ),
                    @OpenApiParam(
                            name = "name",
                            description = "The nickname",
                            required = true
                    ),
            },
            responses = {
                    @OpenApiResponse(
                            status = "200",
                            description = "User successfully removed from lobby"
                    ),
                    @OpenApiResponse(
                            status = "400",
                            description = "Bad request"
                    ),
                    @OpenApiResponse(
                            status = "404",
                            description = "Not found: the provided id corresponds to no known lobby or the provided nickname corresponds to no known player"
                    ),
                    @OpenApiResponse(
                            status = "503",
                            description = "Server offline: server currently unavailable"
                    )
            }
    )
    void DELETEPlayerFromLobby(Context context) throws HttpResponseException;

    static LobbyController of(String root) {
        return new LobbyControllerImpl(root);
    }
}
