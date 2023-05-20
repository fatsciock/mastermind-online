package it.negri.mastermind.client;

import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
import it.negri.mastermind.common.model.Game;
import it.negri.mastermind.common.model.Lobby;
import it.negri.mastermind.common.model.Player;
import it.negri.mastermind.common.model.Role;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class RemoteMastermind extends AbstractHttpClientStub implements Mastermind {

    public RemoteMastermind(URI host) {
        super(host, "mastermind", "0.1.0");
    }
    public RemoteMastermind(String host, int port) {
        this(URI.create("http://" + host + ":" + port));
    }


    // Player


    @Override
    public Player createPlayer(String nick) throws ConflictException, IllegalArgumentException, ServerUnavailableException {
        try {
            return createPlayerAsync(nick).join();
        } catch (CompletionException e) {
            if(e.getCause() instanceof ConflictException) {
                throw getCauseAs(e, ConflictException.class);
            } else if (e.getCause() instanceof ConnectException) {
                throw new ServerUnavailableException(e);
            } else {
                throw new IllegalArgumentException(e);
            }
        }
    }

    private CompletableFuture<Player> createPlayerAsync(String nick) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/players/" + nick))
                .header("Accept", "application/json")
                .POST(body(nick))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Player.class));
    }

    @Override
    public void deletePlayer(String nick) throws MissingException, ServerUnavailableException {
        try {
            deletePlayerAsync(nick).join();
        } catch (CompletionException e) {
            if(e.getCause() instanceof MissingException) {
                throw getCauseAs(e, MissingException.class);
            } else {
                throw new ServerUnavailableException(e);
            }
        }
    }

    private CompletableFuture<?> deletePlayerAsync(String nick) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/players/" + nick))
                .DELETE()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Void.class));
    }

    @Override
    public Player getPlayer(String nick) throws MissingException, ServerUnavailableException {
        try {
            return getPlayerAsync(nick).join();
        } catch (CompletionException e) {
            if(e.getCause() instanceof MissingException) {
                throw getCauseAs(e, MissingException.class);
            } else {
                throw getCauseAs(e, ServerUnavailableException.class);
            }
        }
    }

    private CompletableFuture<Player> getPlayerAsync(String nick) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/players/" + nick))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Player.class));
    }


    // Lobby


    @Override
    public Lobby createLobby() throws ServerUnavailableException {
        try {
            return createLobbyAsync().join();
        } catch (CompletionException e) {
            throw getCauseAs(e, ServerUnavailableException.class);
        }
    }

    private CompletableFuture<Lobby> createLobbyAsync() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies"))
                .header("Accept", "application/json")
                .POST(body())
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Lobby.class));
    }

    @Override
    public void deleteLobby(int lobbyId) throws MissingException, ServerUnavailableException {
        try {
            deleteLobbyAsync(lobbyId).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof MissingException) {
                throw getCauseAs(e, MissingException.class);
            } else {
                throw getCauseAs(e, ServerUnavailableException.class);
            }
        }
    }

    private CompletableFuture<?> deleteLobbyAsync(int lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/" + lobbyId))
                .DELETE()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Void.class));
    }

    @Override
    public Lobby getLobby(int lobbyId) throws MissingException, ServerUnavailableException {
        try {
            return getLobbyAsync(lobbyId).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof MissingException) {
                throw getCauseAs(e, MissingException.class);
            } else {
                throw getCauseAs(e, ServerUnavailableException.class);
            }
        }
    }

    private CompletableFuture<Lobby> getLobbyAsync(int lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/" + lobbyId))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Lobby.class));
    }

    @Override
    public Set<? extends Lobby> getAllLobbies() throws ServerUnavailableException {
        try {
            return new HashSet<>(getAllLobbiesAsync().join());
        } catch (CompletionException e) {
            throw getCauseAs(e, ServerUnavailableException.class);
        }
    }

    private CompletableFuture<List<Lobby>> getAllLobbiesAsync() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies"))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeMany(Lobby.class));
    }

    @Override
    public Lobby addPlayerToLobby(String nick, int lobbyId, Role role) throws MissingException, IllegalArgumentException, ServerUnavailableException {
        try {
            return addPlayerToLobbyAsync(nick, lobbyId, role).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof MissingException) {
                throw getCauseAs(e, MissingException.class);
            } else if (e.getCause() instanceof ConnectException) {
                throw getCauseAs(e, ServerUnavailableException.class);
            } else {
                throw getCauseAs(e, IllegalArgumentException.class);
            }
        }
    }

    private CompletableFuture<Lobby> addPlayerToLobbyAsync(String nick, int lobbyId, Role role) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/" + lobbyId + "/" + nick + "/" + role))
                .header("Accept", "application/json")
                .PUT(body())
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Lobby.class));
    }

    @Override
    public void deletePlayerFromLobby(String nick, int lobbyId) throws MissingException, ServerUnavailableException {
        try {
            deletePlayerFromLobbyAsync(nick, lobbyId).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof MissingException) {
                throw getCauseAs(e, MissingException.class);
            } else {
                throw getCauseAs(e, ServerUnavailableException.class);
            }
        }
    }

    private CompletableFuture<?> deletePlayerFromLobbyAsync(String nick, int lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/" + lobbyId + "/" + nick))
                .DELETE()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Void.class));
    }

    @Override
    public Game startGame(int lobbyId) throws MissingException, ConflictException, IllegalArgumentException, ServerUnavailableException {
        try {
            return startGameAsync(lobbyId).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof ConflictException) {
                throw getCauseAs(e, ConflictException.class);
            } else if (e.getCause() instanceof MissingException) {
                throw getCauseAs(e, MissingException.class);
            } else if (e.getCause() instanceof ServerUnavailableException || e.getCause() instanceof ConnectException) {
                throw getCauseAs(e, ServerUnavailableException.class);
            } else {
                throw getCauseAs(e, IllegalArgumentException.class);
            }
        }
    }

    private CompletableFuture<Game> startGameAsync(int lobbyId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/games/" + lobbyId))
                .header("Accept", "application/json")
                .POST(body())
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Game.class));
    }

    @Override
    public void deleteGame(int gameId) throws MissingException, ServerUnavailableException {
        try {
            deleteGameAsync(gameId).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof MissingException) {
                throw getCauseAs(e, MissingException.class);
            } else {
                throw getCauseAs(e, ServerUnavailableException.class);
            }
        }
    }

    private CompletableFuture<?> deleteGameAsync(int gameId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/games/" + gameId))
                .DELETE()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Void.class));
    }

    @Override
    public Game getGame(int gameId) throws MissingException, ServerUnavailableException {
        try {
            return getGameAsync(gameId).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof MissingException) {
                throw getCauseAs(e, MissingException.class);
            } else {
                throw getCauseAs(e, ServerUnavailableException.class);
            }
        }
    }

    private CompletableFuture<Game> getGameAsync(int gameId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/games/" + gameId))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Game.class));
    }

    @Override
    public String setCode(int gameId, String codeToGuess, String nick) throws MissingException, IllegalArgumentException, ServerUnavailableException {
        try {
            return setCodeAsync(gameId, codeToGuess, nick).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof MissingException) {
                throw getCauseAs(e, MissingException.class);
            } else if (e.getCause() instanceof ServerUnavailableException || e.getCause() instanceof ConnectException) {
                throw getCauseAs(e, ServerUnavailableException.class);
            } else {
                throw getCauseAs(e, IllegalArgumentException.class);
            }
        }
    }

    private CompletableFuture<String> setCodeAsync(int gameId, String codeToGuess, String nick) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/games/" + gameId + "/" + nick))
                .header("Accept", "application/json")
                .PUT(body(codeToGuess))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(String.class));
    }

    @Override
    public Game guessCode(int gameId, String guess, String nick) throws MissingException, ServerUnavailableException {
        try {
            return guessCodeAsync(gameId, guess, nick).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof MissingException) {
                throw getCauseAs(e, MissingException.class);
            } else {
                throw getCauseAs(e, ServerUnavailableException.class);
            }
        }
    }

    private CompletableFuture<Game> guessCodeAsync(int gameId, String guess, String nick) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/games/attempt/" + gameId + "/" + nick))
                .header("Accept", "application/json")
                .PUT(body(guess))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Game.class));
    }
}
