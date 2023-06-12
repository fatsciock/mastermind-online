package it.negri.mastermind.common;

import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.exceptions.ServerUnavailableException;
import it.negri.mastermind.common.model.*;

import java.util.Set;

public interface Mastermind {

    Player createPlayer(final String nick) throws ConflictException, IllegalArgumentException, ServerUnavailableException;

    void deletePlayer(final String nick) throws MissingException, ServerUnavailableException;

    Player getPlayer(final String nick) throws MissingException, ServerUnavailableException;

    Lobby createLobby() throws ServerUnavailableException;

    void deleteLobby(final int lobbyId) throws MissingException, ServerUnavailableException;

    Lobby getLobby(final int lobbyId) throws MissingException, ServerUnavailableException;

    Set<? extends Lobby> getAllLobbies() throws ServerUnavailableException;

    Lobby addPlayerToLobby(final String nick, final int lobbyId, final Role role) throws MissingException, IllegalArgumentException, ServerUnavailableException;

    void deletePlayerFromLobby(final String nick, final int lobbyId) throws MissingException, ServerUnavailableException;

    Game startGame(final int lobbyId) throws MissingException, ConflictException, IllegalArgumentException, ServerUnavailableException;

    void deleteGame(final int gameId) throws MissingException, ServerUnavailableException;

    Game getGame(final int gameId) throws MissingException, ServerUnavailableException;

    String setCode(final int gameId, final String codeToGuess, final String nick) throws MissingException, IllegalArgumentException, ServerUnavailableException;

    Game guessCode(final int gameId, final String guess, final String nick) throws MissingException, IllegalArgumentException, ServerUnavailableException;

    void heartbeat(final String nick) throws MissingException, ServerUnavailableException;

}