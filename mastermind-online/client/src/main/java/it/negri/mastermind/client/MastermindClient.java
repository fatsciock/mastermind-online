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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class MastermindClient extends AbstractHttpClientStub implements Mastermind {

    public MastermindClient(URI host) {
        super(host, "mastermind", "0.1.0");
    }
    public MastermindClient(String host, int port) {
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

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        MastermindClient client = new MastermindClient(host, port);
        Scanner in = new Scanner(System.in);

        Player player = null;

        while(true) {
            System.out.println("Benvenuto! Inserisci un nickname: ");
            String nick = in.nextLine();
            try {
                player = client.createPlayer(nick);
                System.out.println("Giocatore creato!");
                Thread.sleep(1500);
                break;
            } catch (ConflictException e) {
                System.err.println("Il nickname " + nick + " esiste gia'.");
            } catch (IllegalArgumentException e ) {
                System.err.println("Il nickname non puo' essere vuoto");
            } catch (ServerUnavailableException e) {
                printServerUnavailableException();
            } catch (InterruptedException e) {
                printInterruptedExceptionAndExit();
            }
        }

        boolean exit = false;

        while(!exit) {
            System.out.println("\n---------------MENU---------------");
            System.out.println("Seleziona una delle voci presenti");
            System.out.println("1) Crea una lobby");
            System.out.println("2) Visualizza lobby disponibili");
            System.out.println("3) Esci");

            Lobby lobby = null;
            Game game = null;
            player.setAsInactive();
            String option = in.nextLine();

            switch (option) {
                case "1" -> {
                    switch (selectRole(in)) {
                        case ENCODER -> player.setAsEncoder();
                        case DECODER -> player.setAsDecoder();
                    }

                    try {
                        lobby = client.createLobby();
                    } catch (ServerUnavailableException e) {
                        printServerUnavailableException();
                        continue;
                    }
                    System.out.println("\n----------------------------------");
                    System.out.println("Lobby " + lobby.getId() + " creata.");

                    try {
                        lobby = client.addPlayerToLobby(player.getNickname(), lobby.getId(), player.getRole());
                    } catch (MissingException | IllegalArgumentException e) {
                        System.err.println(e.getMessage());
                        continue;
                    } catch (ServerUnavailableException e) {
                        printServerUnavailableException();
                        continue;
                    }

                    System.out.println("In attesa di un altro giocatore...");
                    try {
                        while (true) {
                            lobby = client.getLobby(lobby.getId());
                            if(lobby.isFull()) {
                                System.out.println("Un giocatore si è unito alla partita!");
                                break;
                            }
                            Thread.sleep(500);
                        }
                    } catch (MissingException e) {
                        System.err.println("Non esiste alcuna lobby con id " + lobby.getId());
                        continue;
                    } catch (ServerUnavailableException e) {
                        printServerUnavailableException();
                        continue;
                    } catch (InterruptedException e) {
                        printInterruptedExceptionAndExit();
                    }
                }
                case "2" -> {
                    boolean exitFromLobbiesList = false;
                    int selectedLobbyId = -1;
                    while(!exitFromLobbiesList) {
                        try {
                            var lobbies = client.getAllLobbies();
                            selectedLobbyId = -1;
                            System.out.println("\n--------------LOBBIES--------------");
                            for (Lobby l : lobbies) {
                                if (!l.isFull()) {
                                    System.out.println("Lobby " + l.getId() + " - Ruolo mancante: " + (l.getPlayerA() != null ? getOppositeRole(l.getPlayerA().getRole()) : getOppositeRole(l.getPlayerB().getRole())));
                                }
                            }

                            System.out.println("\nR) Refresh lista");
                            System.out.println("B) Torna al menu");
                            System.out.print("Inserisci codice lobby a cui connettersi: ");

                            String lobbyOption = in.nextLine();

                            switch (lobbyOption) {
                                case "R" -> {
                                    continue;
                                }
                                case "B" -> {
                                    exitFromLobbiesList = true;
                                    continue;
                                }
                                default -> {
                                    if(lobbyOption.matches("[0-9]+")) {
                                        selectedLobbyId = Integer.parseInt(lobbyOption);
                                        final int tempSelectedLobbyId = selectedLobbyId;
                                        if (lobbies.stream().anyMatch(l -> l.getId() == tempSelectedLobbyId)) {
                                            switch (getUnchosenRole(lobbies, selectedLobbyId)) {
                                                case ENCODER -> player.setAsEncoder();
                                                case DECODER -> player.setAsDecoder();
                                            }
                                        } else {
                                            System.err.println("Inserisci un numero di una lobby esistente");
                                            continue;
                                        }
                                    } else {
                                        System.err.println("Inserisci solo dei numeri, non altri tipi di carattere");
                                        continue;
                                    }
                                }
                            }

                            try {
                                lobby = client.addPlayerToLobby(player.getNickname(), selectedLobbyId, player.getRole());
                                exitFromLobbiesList = true;
                            } catch (MissingException | IllegalArgumentException e) {
                                System.err.println(e.getMessage());
                            }
                        } catch (ServerUnavailableException e) {
                            printServerUnavailableException();
                        }
                    }
                    if (selectedLobbyId == -1) {
                        continue;
                    }
                    try {
                        client.startGame(lobby.getId());
                    } catch (MissingException e) {
                        System.err.println("Non esiste alcuna lobby con id " + lobby.getId());
                        continue;
                    } catch (ConflictException e) {
                        System.err.println("Nella lobby " + lobby.getId() + " e' gia' in corso un game");
                        continue;
                    } catch (IllegalArgumentException e) {
                        System.err.println("La lobby " + lobby.getId() + " non e' piena, il game non puo' iniziare");
                        continue;
                    }  catch (ServerUnavailableException e){
                        printServerUnavailableException();
                        continue;
                    }
                }
                case "3" -> {
                    try {
                        client.deletePlayer(player.getNickname());
                    } catch (MissingException e) {
                        System.err.println("Non esiste alcun player con nickname " + player.getNickname());
                    } catch (ServerUnavailableException e) {
                        printServerUnavailableException();
                    }
                    exit = true;
                }
                default -> {
                    System.err.println("Scegliere un'opzione tra 1 e 4");
                    continue;
                }
            }

            if (exit) {
                break;
            }

            while (true) {
                try {
                    Thread.sleep(500);
                    game = client.getGame(lobby.getId());
                    break;
                } catch (MissingException e) {
                    System.err.println("Creazione game in corso, attendere...");
                } catch (ServerUnavailableException e) {
                    printServerUnavailableException();
                } catch (InterruptedException e) {
                    printInterruptedExceptionAndExit();
                }
            }

            boolean isGameFinished = false;

            while (!isGameFinished) {
                System.out.println("GIOCO");
            }

            switch (option) {
                case "1": {
                    while (true) {
                        try {
                            lobby = client.getLobby(lobby.getId());
                            if (lobby.getPlayerA() == null || lobby.getPlayerB() == null) {
                                break;
                            }
                            Thread.sleep(500);
                        } catch (MissingException e) {
                            System.err.println(e.getMessage());
                            break;
                        } catch (ServerUnavailableException e) {
                            printServerUnavailableException();
                            break;
                        } catch (InterruptedException e) {
                            printInterruptedExceptionAndExit();
                        }
                    }
                    try {
                        client.deleteLobby(lobby.getId());
                    } catch (MissingException e) {
                        System.err.println(e.getMessage());
                        break;
                    } catch (ServerUnavailableException e) {
                        printServerUnavailableException();
                    }
                }
                case "2": {
                    try {
                        client.deletePlayerFromLobby(player.getNickname(), lobby.getId());
                    } catch (MissingException e) {
                        System.err.println(e.getMessage());
                    } catch (ServerUnavailableException e) {
                        printServerUnavailableException();
                    }
                }
            }
        }

        System.out.println("Uscita dal gioco...");
        System.exit(0);
    }

    private static void printServerUnavailableException() {
        System.err.println("Serve attualmente non raggiungibile");
    }

    private static void printInterruptedExceptionAndExit() {
        System.err.println("Errore interno al client, chiusura in corso...");
        System.exit(1);
    }

    private static Role selectRole(Scanner in){
        while(true) {
            System.out.println("\nSeleziona un ruolo:");
            System.out.println("1) Codificatore");
            System.out.println("2) Decodificatore");
            switch (in.nextLine()) {
                case "1" -> {
                    return Role.ENCODER;
                }
                case "2" -> {
                    return Role.DECODER;
                }
                default -> {
                    System.err.println("Scegliere un'opzione tra 1 e 2");
                }
            }
        }
    }

    private static Role getOppositeRole(Role role) {
        if (Objects.requireNonNull(role) == Role.DECODER) {
            return Role.ENCODER;
        }
        return Role.DECODER;
    }

    private static Role getUnchosenRole(Set<? extends Lobby> lobbies, int selectedLobbyId) {
        var result = Role.INACTIVE;
        for (Lobby l : lobbies) {
            if (l.getId() == selectedLobbyId) {
                if (l.getPlayerA() != null) {
                    result = getOppositeRole(l.getPlayerA().getRole());
                } else {
                    result = getOppositeRole(l.getPlayerB().getRole());
                }
                break;
            }
        }
        return result;
    }

}
