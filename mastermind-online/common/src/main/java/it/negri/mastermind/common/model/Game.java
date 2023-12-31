package it.negri.mastermind.common.model;

import it.negri.mastermind.common.utils.Utils;

import java.util.*;

public class Game {
    private final int id;
    private Player playerA;
    private Player playerB;
    private Player winner = null;
    private int remainingAttempts;
    private final List<String> attempts;
    private final List<Map<HintLabel, Integer>> hintPerAttempt;
    private String code;

    public Game(Lobby lobby) {
        this.id = lobby.getId();
        this.playerA = lobby.getPlayerA();
        this.playerB = lobby.getPlayerB();
        this.remainingAttempts = Utils.getDefaultAttempts();
        this.attempts = new ArrayList<>();
        this.hintPerAttempt = new ArrayList<>();
    }

    public Game(int id, Player playerA, Player playerB, Player winner, int remainingAttempts, List<String> attempts, List<Map<HintLabel, Integer>> hintPerAttempt, String code) {
        this.id = id;
        this.playerA = playerA;
        this.playerB = playerB;
        this.winner = winner;
        this.remainingAttempts = remainingAttempts;
        this.attempts = attempts;
        this.hintPerAttempt = hintPerAttempt;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public Player getPlayerA() {
        return playerA;
    }

    public void setPlayerA(Player playerA) {
        this.playerA = playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public void setPlayerB(Player playerB) {
        this.playerB = playerB;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public void setRemainingAttempts(int remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }

    public List<String> getAttempts() {
        return attempts;
    }

    public List<Map<HintLabel, Integer>> getHintPerAttempt() {
        return hintPerAttempt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Game tryToGuessCode(final String guess) {
        remainingAttempts -= 1;
        attempts.add(guess);
        hintPerAttempt.add(getHintFromCode(guess));

        return this;
    }

    private Map<HintLabel, Integer> getHintFromCode(final String guess) {
        var result = new HashMap<HintLabel, Integer>();

        if (guess.equals(code)) {
            winner = playerA.getRole() == Role.DECODER ? playerA : playerB;
            result.put(HintLabel.RIGHT_NUMBER_IN_WRONG_PLACE, 0);
            result.put(HintLabel.RIGHT_NUMBER_IN_RIGHT_PLACE, Utils.getCodeLength());
            return result;
        } else if (remainingAttempts == 0) {
            winner = playerA.getRole() == Role.ENCODER ? playerA : playerB;
        }

        int cont = 0; //contatore usato per entrambi
        List<Character> unmatchedCode = new ArrayList<>();
        List<Character> unmatchedGuess = new ArrayList<>();

        // Calcolo RIGHT_NUMBER_IN_RIGHT_PLACE
        for(int i=0; i<Utils.getCodeLength(); i++) {
            if(guess.charAt(i) == code.charAt(i)) {
                cont++;
            } else {
                unmatchedCode.add(code.charAt(i));
                unmatchedGuess.add(guess.charAt(i));
            }
        }
        result.put(HintLabel.RIGHT_NUMBER_IN_RIGHT_PLACE, cont);
        cont = 0;

        // Calcolo RIGHT_NUMBER_IN_WRONG_PLACE
        for (char guessChar : unmatchedGuess) {
            if (unmatchedCode.contains(guessChar)) {
                cont++;
                unmatchedCode.remove((Character) guessChar);
            }
        }
        result.put(HintLabel.RIGHT_NUMBER_IN_WRONG_PLACE, cont);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id && remainingAttempts == game.remainingAttempts && Objects.equals(playerA, game.playerA) && Objects.equals(playerB, game.playerB) && Objects.equals(winner, game.winner) && Objects.equals(attempts, game.attempts) && Objects.equals(hintPerAttempt, game.hintPerAttempt) && Objects.equals(code, game.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playerA, playerB, winner, remainingAttempts, attempts, hintPerAttempt, code);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", playerA=" + playerA +
                ", playerB=" + playerB +
                ", winner=" + winner +
                ", remainingAttempts=" + remainingAttempts +
                ", attempts=" + attempts +
                ", hintPerAttempt=" + hintPerAttempt +
                ", code='" + code + '\'' +
                '}';
    }
}
