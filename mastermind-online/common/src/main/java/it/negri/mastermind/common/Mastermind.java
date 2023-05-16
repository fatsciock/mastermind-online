package it.negri.mastermind.common;

import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.model.*;

import java.util.Set;

public interface Mastermind {

    Player createPlayer(final String nick) throws ConflictException, IllegalArgumentException;

    void deletePlayer(final String nick) throws MissingException;

    Player getPlayer(final String nick) throws MissingException;

    Lobby createLobby();

    void deleteLobby(final int lobbyId) throws MissingException;

    Lobby getLobby(final int lobbyId) throws MissingException;

    Set<? extends Lobby> getAllLobbies();

    Lobby addPlayerToLobby(final String nick, final int lobbyId, final Role role) throws MissingException, IllegalArgumentException;

    void deletePlayerFromLobby(final String nick, final int lobbyId) throws MissingException;

    Game startGame(final int lobbyId) throws MissingException, ConflictException, IllegalArgumentException;

    Game getGame(final int gameId) throws MissingException;

    void setCode(final int gameId, final String codeToGuess, final String nick) throws MissingException, IllegalArgumentException;

    Game guessCode(final int gameId, final String guess, final String nick) throws MissingException;

}