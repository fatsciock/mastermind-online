package it.negri.mastermind.common.model;

import java.util.Objects;

public class Lobby {

    private final int id;
    private Player playerA;
    private Player playerB;

    public Lobby(int id) {
        this.id = id;
    }

    public Lobby(int id, Player playerA, Player playerB) {
        this.id = id;
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public boolean isFull() {
        return (playerA != null && playerB != null);
    }

    public boolean isEmpty() {
        return (playerA == null && playerB == null);
    }

    public boolean isAvailableRole(Role role) {
        return isEmpty() || (!isFull() &&
                ((playerB == null && playerA.getRole() != role) ||
                        (playerA == null && playerB.getRole() != role)));
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
}
