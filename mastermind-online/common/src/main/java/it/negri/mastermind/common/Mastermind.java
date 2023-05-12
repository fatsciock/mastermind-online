package it.negri.mastermind.common;

import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.model.Lobby;
import it.negri.mastermind.common.model.Match;
import it.negri.mastermind.common.model.Player;
import it.negri.mastermind.common.model.ResultLabel;

import java.util.Map;
import java.util.Set;

public interface Mastermind {

    Player createPlayer(final String nick) throws ConflictException;

    void deletePlayer(final String nick) throws ConflictException;

    Player getPlayer(final String nick) throws MissingException;

    Lobby createLobby();

    void deleteLobby(final int lobbyId) throws MissingException;

    Lobby getLobby(final int lobbyId) throws MissingException;

    Set<? extends Lobby> getAllLobbies();

    //Deve in automatico settare correttamente il ruolo del secondo player
    Lobby addPlayerToLobby(final String nick, final int lobbyId) throws MissingException;

    void deletePlayerFromLobby(final String nick, final int lobbyId) throws MissingException;

    Match startGame(final int lobbyId) throws MissingException, ConflictException, IllegalStateException;

    void setCode(String guess) throws MissingException, IllegalArgumentException;

    Map<ResultLabel, Integer> guessCode(String guess) throws MissingException;

}