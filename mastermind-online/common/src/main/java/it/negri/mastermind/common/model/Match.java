package it.negri.mastermind.common.model;

import java.util.List;
import java.util.Objects;

public class Match {

    private static final int DEFAULT_ATTEMPTS = 9;

    private final int id;
    private Player playerA;
    private Player playerB;

    private int remainingAttempts = DEFAULT_ATTEMPTS;
    private List<String> attempts;
    private String code;

    public Match(int id, Player playerA, Player playerB) {
        this.id = id;
        this.playerA = playerA;
        this.playerB = playerB;
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

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public void setRemainingAttempts(int remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }

    public List<String> getAttempts() {
        return attempts;
    }

    public void setAttempts(List<String> attempts) {
        this.attempts = attempts;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return id == match.id && remainingAttempts == match.remainingAttempts && Objects.equals(playerA, match.playerA) && Objects.equals(playerB, match.playerB) && Objects.equals(attempts, match.attempts) && Objects.equals(code, match.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playerA, playerB, remainingAttempts, attempts, code);
    }

    @Override
    public String toString() {
        return "Match{" +
                "id=" + id +
                ", playerA=" + playerA +
                ", playerB=" + playerB +
                ", remainingAttempts=" + remainingAttempts +
                ", attempts=" + attempts +
                ", code='" + code + '\'' +
                '}';
    }
}
