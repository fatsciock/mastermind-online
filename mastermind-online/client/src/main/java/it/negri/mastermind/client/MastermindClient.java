package it.negri.mastermind.client;

import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.PlayerTimeoutException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
import it.negri.mastermind.common.model.*;
import it.negri.mastermind.common.utils.Utils;

import java.util.*;

public class MastermindClient {

    private static final int TIMEOUT = 90; //45 secondi, il controllo viene fatto ogni 0.5s per questo il valore è 90

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        RemoteMastermind client = new RemoteMastermind(host, port);
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
            System.out.flush();
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
                    } catch (MissingException e) {
                        if (e.getMessage().contains("user")) {
                            printMissingPlayerException(player);
                        } else {
                            printMissingLobbyException(lobby);
                        }
                        continue;
                    } catch (IllegalArgumentException e) {
                        if (e.getMessage().contains("MUST")) {
                            printSelectRoleException();
                        } else if (e.getMessage().contains("Lobby")) {
                            printFullLobbyException(lobby);
                        } else {
                            printRoleChosenException(player);
                        }
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
                                    System.out.println("Lobby " + l.getId() + " - Ruolo mancante: " +
                                            (l.getPlayerA() != null ? getOppositeRole(l.getPlayerA().getRole()) : getOppositeRole(l.getPlayerB().getRole())) +
                                            " - Player collegato: " + (l.getPlayerA() != null ? l.getPlayerA().getNickname() : l.getPlayerB().getRole()));
                                }
                            }

                            System.out.println("\nR) Refresh lista");
                            System.out.println("B) Torna al menu");
                            System.out.print("Inserisci codice lobby a cui connettersi: ");

                            String lobbyOption = in.nextLine();

                            switch (lobbyOption) {
                                case "R", "r" -> {
                                    continue;
                                }
                                case "B", "b" -> {
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
                            } catch (MissingException e) {
                                if (e.getMessage().contains("user")) {
                                    printMissingPlayerException(player);
                                } else {
                                    printMissingLobbyException(lobby);
                                }
                            } catch (IllegalArgumentException e) {
                                if (e.getMessage().contains("MUST")) {
                                    printSelectRoleException();
                                } else if (e.getMessage().contains("Lobby")) {
                                    printFullLobbyException(lobby);
                                } else {
                                    printRoleChosenException(player);
                                }
                            } catch (ServerUnavailableException e) {
                                printServerUnavailableException();
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
                        printMissingLobbyException(lobby);
                        continue;
                    } catch (ConflictException e) {
                        printGameAlreadyStartedException(lobby);
                        continue;
                    } catch (IllegalArgumentException e) {
                        printNotFullLobbyException(lobby);
                        continue;
                    }  catch (ServerUnavailableException e) {
                        printServerUnavailableException();
                        continue;
                    }
                }
                case "3" -> {
                    try {
                        client.deletePlayer(player.getNickname());
                    } catch (MissingException e) {
                        printMissingPlayerException(player);
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

            playGame(client, player, lobby, game, in);

            try {
                client.deletePlayerFromLobby(player.getNickname(), lobby.getId());
            } catch (MissingException e) {
                System.err.println(e.getMessage());
            } catch (ServerUnavailableException e) {
                printServerUnavailableException();
            }
        }

        System.out.println("Uscita dal gioco...");
        System.exit(0);
    }

    private static void playGame(RemoteMastermind client, Player player, Lobby lobby, Game game, Scanner in) {
        System.out.println("---------INIZIO GAME---------");
        switch (player.getRole()) {
            case DECODER -> {
                System.out.println("Sei il decodificatore");
                System.out.println("Attendi che il codificatore scelga un codice...");
                int countdown = 0;
                try {
                    while (true) {
                        if(countdown >= TIMEOUT) {
                            throw new PlayerTimeoutException();
                        }
                        game = client.getGame(game.getId());
                        if (game.getCode() == null || game.getCode().isBlank()) {
                            Thread.sleep(500);
                            countdown++;
                            continue;
                        }
                        break;
                    }
                } catch (MissingException e) {
                    printMissingGameExpection(game);
                } catch (ServerUnavailableException e) {
                    printServerUnavailableException();
                    break;
                } catch (PlayerTimeoutException e) {
                    printPlayerTimeoutException();
                    break;
                } catch (InterruptedException e) {
                    printInterruptedExceptionAndExit();
                }
                System.out.println("Il codificatore ha scelto il codice");
                String guess;
                int remainingAttempts;
                int actualAttempt = 0;

                while (true) {
                    remainingAttempts = game.getRemainingAttempts();
                    System.out.println("Tentativi rimasti: " + remainingAttempts);
                    System.out.println("Inserisci il tuo tentativo (ricorda, deve essere composto da " + Utils.getCodeLength() + " cifre DECIMALI (0-9): ");
                    guess = in.nextLine();
                    try {
                        game = client.guessCode(game.getId(), guess, player.getNickname());

                        if (game.getWinner() != null) {
                            if (game.getWinner().getNickname().equals(player.getNickname())) {
                                System.out.println("COMPLIMENTI! HAI VINTO!");
                            } else {
                                System.out.println("Mi dispiace, hai perso :(");
                            }
                            break;
                        }
                        System.out.println("Hai provato il codice " + guess);
                        var hint = game.getHintPerAttempt().get(actualAttempt);
                        System.out.println("Cifre giuste al posto giusto: " + hint.get(HintLabel.RIGHT_NUMBER_IN_RIGHT_PLACE));
                        System.out.println("Cifre giuste al posto sbagliato: " + hint.get(HintLabel.RIGHT_NUMBER_IN_WRONG_PLACE));
                        System.out.println("--------------------------------------------");
                        actualAttempt++;

                    } catch (MissingException e) {
                        if (e.getMessage().contains("user")) {
                            printMissingPlayerException(player);
                        } else {
                            printMissingGameExpection(game);
                        }
                    } catch (IllegalArgumentException e) {
                        if (e.getMessage().contains("Code")) {
                            printErrorInCodeException();
                        } else if (e.getMessage().contains("game")) {
                            printNotInGameException(game);
                        } else {
                            System.err.println("Ruolo errato");
                        }
                    } catch (ServerUnavailableException e) {
                        printServerUnavailableException();
                    }
                }
            }
            case ENCODER -> {
                System.out.println("Sei il codificatore");
                String codeToGuess;
                int remainingAttempts = game.getRemainingAttempts();

                while (true) {
                    System.out.println("Inserisci il codice da far indovinare (ricorda, deve essere composto da " + Utils.getCodeLength() + " cifre DECIMALI (0-9): ");
                    codeToGuess = in.nextLine();
                    try {
                        System.out.println("Codice " + client.setCode(game.getId(), codeToGuess, player.getNickname()) + " impostato");
                        break;
                    } catch (MissingException e) {
                        if (e.getMessage().contains("user")) {
                            printMissingPlayerException(player);
                        } else {
                            printMissingGameExpection(game);
                        }
                    }  catch (IllegalArgumentException e) {
                        if (e.getMessage().contains("Code")) {
                            printErrorInCodeException();
                        } else if (e.getMessage().contains("game")) {
                            printNotInGameException(game);
                        } else {
                            System.err.println("Ruolo errato");
                        }
                    } catch (ServerUnavailableException e) {
                        printServerUnavailableException();
                    }
                }

                int countdown = 0;
                try {
                    while (true) {
                        if(countdown >= TIMEOUT) {
                            throw new PlayerTimeoutException();
                        }
                        game = client.getGame(game.getId());
                        if (game.getRemainingAttempts() == remainingAttempts) {
                            Thread.sleep(500);
                            countdown++;
                            continue;
                        }
                        if (game.getWinner() != null) {
                            if (game.getWinner().getNickname().equals(player.getNickname())) {
                                System.out.println("COMPLIMENTI! HAI VINTO!");
                            } else {
                                System.out.println("Mi dispiace, hai perso :(");
                            }
                            break;
                        }
                        int newAttempt = remainingAttempts - game.getRemainingAttempts();
                        remainingAttempts = game.getRemainingAttempts();
                        var decoderAttempts = game.getAttempts();

                        for (int i = 0; i < decoderAttempts.size(); i++) {
                            if (i >= decoderAttempts.size() - newAttempt) {
                                System.out.println("Il decodificatore ha provato il codice " + decoderAttempts.get(i));
                            }
                        }
                    }
                } catch (MissingException e) {
                    printMissingGameExpection(game);
                } catch (ServerUnavailableException e) {
                    printServerUnavailableException();
                } catch (PlayerTimeoutException e) {
                    printPlayerTimeoutException();
                } catch (InterruptedException e) {
                    printInterruptedExceptionAndExit();
                }
            }
        }
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


    // Print eccezioni

    private static void printInterruptedExceptionAndExit() {
        System.err.println("Errore interno al client, chiusura in corso...");
        System.exit(1);
    }

    private static void printPlayerTimeoutException() {
        System.err.println("Tempo scaduto per l'avversario, ritorno alla lobby in corso...");
    }

    private static void printGameAlreadyStartedException(Lobby lobby) {
        System.err.println("Nella lobby " + lobby.getId() + " e' gia' in corso un game");
    }

    private static void printNotInGameException(Game game) {
        System.err.println("Non fai parte del game " + game.getId());
    }

    private static void printMissingGameExpection(Game game) {
        System.err.println("Non esiste un game con id " + game.getId());
    }

    private static void printServerUnavailableException() {
        System.err.println("Serve attualmente non raggiungibile");
    }

    private static void printMissingPlayerException(Player player) {
        System.err.println("Non esiste un giocatore con nick " + player.getNickname());
    }

    private static void printMissingLobbyException(Lobby lobby) {
        System.err.println("Non esiste una lobby con id " + lobby.getId());
    }

    private static void printFullLobbyException(Lobby lobby) {
        System.err.println("La lobby " + lobby.getId() + " e' piena, selezionane un'altra");
    }

    private static void printNotFullLobbyException(Lobby lobby) {
        System.err.println("La lobby " + lobby.getId() + " non e' piena, il game non puo' iniziare");
    }

    private static void printSelectRoleException() {
        System.err.println("Non hai selezionato alcun ruolo, selezionarne uno");
    }

    private static void printRoleChosenException(Player player) {
        System.err.println("Ruolo " + player.getRole() + " già scelto, seleziona l'altro ruolo");
    }

    private static void printErrorInCodeException() {
        System.err.println("Il codice non e' valido. Deve essere composto da solo cifre decimali (0-9) e lungo " + Utils.getCodeLength() );
    }
}
