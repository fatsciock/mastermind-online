package it.negri.mastermind.client;

import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
import it.negri.mastermind.common.model.Game;
import it.negri.mastermind.common.model.Lobby;
import it.negri.mastermind.common.model.Player;
import it.negri.mastermind.common.model.Role;

import java.util.*;

public class MastermindClient {

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
