package it.negri.mastermind.common;

import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.model.*;
import it.negri.mastermind.common.utils.Utils;

import java.util.*;

public class LocalMastermind implements Mastermind {

    private final Map<String, Player> usersByNickname = new HashMap<>();
    private final Map<Integer, Lobby> lobbiesById = new HashMap<>();
    private final Map<Integer, Game> gamesById = new HashMap<>();
    private final HeartbeatManager heartbeatManager = new HeartbeatManager(this);

    @Override
    public Player createPlayer(final String nick) throws ConflictException, IllegalArgumentException {
        var newPlayer = new Player(nick);
        if(newPlayer.getNickname().isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be empty string");
        }
        if(usersByNickname.containsKey(newPlayer.getNickname())) {
            throw new ConflictException("Nickname " + newPlayer.getNickname() + " already exists");
        }
        usersByNickname.put(newPlayer.getNickname(), newPlayer);
        heartbeatManager.registerNewPlayer(newPlayer.getNickname());
        return newPlayer;
    }

    @Override
    public void deletePlayer(final String nick) throws MissingException {
        var playerToDelete = getPlayer(nick);
        usersByNickname.remove(playerToDelete.getNickname());
        heartbeatManager.deletePlayer(playerToDelete.getNickname());
    }

    @Override
    public Player getPlayer(final String nick) throws MissingException {
        if(!usersByNickname.containsKey(nick)) {
            throw new MissingException("There is no user with nickname " + nick);
        }
        return usersByNickname.get(nick);
    }

    @Override
    public Lobby createLobby() {
        int newId;
        if(lobbiesById.isEmpty()) {
            newId = 1;
        } else {
            newId = Collections.max(lobbiesById.keySet()) + 1;
        }
        var newLobby = new Lobby(newId);
        lobbiesById.put(newLobby.getId(), newLobby);
        return newLobby;
    }

    @Override
    public void deleteLobby(final int id) throws MissingException {
        var lobbyToDelete = getLobby(id);
        lobbiesById.remove(lobbyToDelete.getId());
    }

    @Override
    public Lobby getLobby(final int id) throws MissingException {
        if(!lobbiesById.containsKey(id)) {
            throw new MissingException("There is no lobby with id " + id);
        }
        return lobbiesById.get(id);
    }

    @Override
    public Set<? extends Lobby> getAllLobbies() {
        return new HashSet<>(lobbiesById.values());
    }

    @Override
    public Lobby addPlayerToLobby(final String nick, final int lobbyId, final Role role) throws MissingException, IllegalArgumentException {
        var player = getPlayer(nick);
        var lobby = getLobby(lobbyId);

        switch (role) {
            case DECODER -> player.setAsDecoder();
            case ENCODER -> player.setAsEncoder();
            case INACTIVE -> throw new IllegalArgumentException("A player MUST chose a role");
        }

        if(lobby.isFull()) {
            throw new IllegalArgumentException("Lobby " + lobbyId + " is full");
        } else if (!lobby.isAvailableRole(player.getRole())) {
            throw new IllegalArgumentException("Role " + role + " is already chosen");
        } else if (lobby.isEmpty() || lobby.getPlayerA() == null) {
            lobby.setPlayerA(player);
        } else if (lobby.getPlayerB() == null){
            lobby.setPlayerB(player);
        }
        heartbeatManager.registerPlayerToLobby(nick, lobbyId);

        return lobby;
    }

    @Override
    public void deletePlayerFromLobby(final String nick, final int lobbyId) throws MissingException {
        var player = getPlayer(nick);
        var lobby = getLobby(lobbyId);

        if (lobby.getPlayerA() == player) {
            lobby.setPlayerA(null);
        } else if (lobby.getPlayerB() == player) {
            lobby.setPlayerB(null);
        } else {
            throw new MissingException("There is no user " + nick + " in the lobby " + lobbyId);
        }
        heartbeatManager.deletePlayerFromLobby(nick, lobbyId);

        if(lobby.isEmpty()) {
            deleteLobby(lobbyId);
            if(gamesById.containsKey(lobbyId)) {
                deleteGame(lobbyId);
            }
        }
    }

    @Override
    public Game startGame(final int lobbyId) throws MissingException, ConflictException, IllegalArgumentException {
        var lobby = getLobby(lobbyId);

        if(gamesById.containsKey(lobby.getId())) {
            throw new ConflictException("Match " + lobby.getId() + " already started");
        }
        if(!lobby.isFull()) {
            throw new IllegalArgumentException("Lobby " + lobby.getId() + " is not full");
        }

        var newGame = new Game(lobby);
        gamesById.put(newGame.getId(), newGame);
        return newGame;
    }

    @Override
    public void deleteGame(final int gameId) throws MissingException {
        var gameToDelete = getGame(gameId);
        gamesById.remove(gameToDelete.getId());
    }

    @Override
    public Game getGame(final int gameId) throws MissingException {
        if(!gamesById.containsKey(gameId)) {
            throw new MissingException("There is no game with id " + gameId);
        }
        return gamesById.get(gameId);
    }

    @Override
    public String setCode(final int gameId, final String codeToGuess, final String nick) throws MissingException, IllegalArgumentException {
        var game = getGame(gameId);
        var player = getPlayer(nick);

        checkPlayer(game, player);
        checkRole(player, Role.ENCODER);
        checkCode(codeToGuess);

        game.setCode(codeToGuess);
        return game.getCode();
    }

    @Override
    public Game guessCode(final int gameId, final String guess, final String nick) throws MissingException, IllegalArgumentException {
        var game = getGame(gameId);
        var player = getPlayer(nick);

        checkPlayer(game, player);
        checkRole(player, Role.DECODER);
        checkCode(guess);

        return game.tryToGuessCode(guess);
    }

    @Override
    public void heartbeat(String nick) throws MissingException {
        var player = getPlayer(nick);

        heartbeatManager.heartbeat(player.getNickname());
    }

    private void checkPlayer(Game game, Player player) throws IllegalArgumentException {
        if(game.getPlayerA() != player && game.getPlayerB() != player) {
            throw new IllegalArgumentException("Player " + player.getNickname() + " is not in game " + game.getId());
        }
    }

    private void checkRole(Player player, Role role) throws IllegalArgumentException {
        if(player.getRole() != role) {
            throw new IllegalArgumentException("Player " + player.getNickname() + " is not the " + role.toString().toUpperCase());
        }
    }

    private void checkCode(String code) throws IllegalArgumentException {
        if(!Utils.validateCode(code)) {
            throw new IllegalArgumentException("Code " + code + " is not valid. It must be a code of " + Utils.getCodeLength() + " decimal digits (0-9)");
        }
    }
}
