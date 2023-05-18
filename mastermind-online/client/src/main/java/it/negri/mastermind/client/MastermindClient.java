package it.negri.mastermind.client;

import it.negri.mastermind.common.Mastermind;
import it.negri.mastermind.common.exceptions.ConflictException;
import it.negri.mastermind.common.exceptions.MissingException;
import it.negri.mastermind.common.model.Game;
import it.negri.mastermind.common.model.Lobby;
import it.negri.mastermind.common.model.Player;
import it.negri.mastermind.common.model.Role;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MastermindClient extends AbstractHttpClientStub implements Mastermind {

    public MastermindClient(URI host) {
        super(host, "mastermind", "0.1.0");
    }
    public MastermindClient(String host, int port) {
        this(URI.create("http://" + host + ":" + port));
    }

    @Override
    public Player createPlayer(String nick) throws ConflictException, IllegalArgumentException {
        return null;
    }

    private CompletableFuture<?> createPlayerAsync(String nick) {
        return null;
    }

    @Override
    public void deletePlayer(String nick) throws MissingException {

    }

    private CompletableFuture<?> deletePlayerAsync(String nick) {
        return null;
    }

    @Override
    public Player getPlayer(String nick) throws MissingException {
        return null;
    }

    private CompletableFuture<?> getPlayerAsync(String nick) {
        return null;
    }

    @Override
    public Lobby createLobby() {
        return null;
    }

    private CompletableFuture<?> createLobbyAsync() {
        return null;
    }

    @Override
    public void deleteLobby(int lobbyId) throws MissingException {

    }

    private CompletableFuture<?> deleteLobbyAsync(int lobbyId) {
        return null;
    }

    @Override
    public Lobby getLobby(int lobbyId) throws MissingException {
        return null;
    }

    private CompletableFuture<?> getLobbyAsync(int lobbyId) {
        return null;
    }

    @Override
    public Set<? extends Lobby> getAllLobbies() {
        return null;
    }

    private CompletableFuture<?> getAllLobbiesAsync() {
        return null;
    }

    @Override
    public Lobby addPlayerToLobby(String nick, int lobbyId, Role role) throws MissingException, IllegalArgumentException {
        return null;
    }

    private CompletableFuture<?> addPlayerToLobbyAsync(String nick, int lobbyId, Role role) {
        return null;
    }

    @Override
    public void deletePlayerFromLobby(String nick, int lobbyId) throws MissingException {

    }

    private CompletableFuture<?> deletePlayerFromLobbyAsync(String nick, int lobbyId) {
        return null;
    }

    @Override
    public Game startGame(int lobbyId) throws MissingException, ConflictException, IllegalArgumentException {
        return null;
    }

    private CompletableFuture<?> startGameAsync(int lobbyId) {
        return null;
    }

    @Override
    public void deleteGame(int gameId) throws MissingException {

    }

    private CompletableFuture<?> deleteGameAsync(int gameId) {
        return null;
    }

    @Override
    public Game getGame(int gameId) throws MissingException {
        return null;
    }

    private CompletableFuture<?> getGameAsync(int gameId) {
        return null;
    }

    @Override
    public String setCode(int gameId, String codeToGuess, String nick) throws MissingException, IllegalArgumentException {
        return null;
    }

    private CompletableFuture<?> setCodeAsync(int gameId, String codeToGuess, String nick) {
        return null;
    }

    @Override
    public Game guessCode(int gameId, String guess, String nick) throws MissingException {
        return null;
    }

    private CompletableFuture<?> guessCodeAsync(int gameId, String guess, String nick) {
        return null;
    }

    public static void main(String[] args) {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        MastermindClient client = new MastermindClient(host, port);
    }
}
