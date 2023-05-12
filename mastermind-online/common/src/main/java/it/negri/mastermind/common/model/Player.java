package it.negri.mastermind.common.model;

import java.util.Objects;

public class Player {

    private final String nickname;

    private Role role;

    public Player(String nickname) {
        this(nickname, Role.INACTIVE);
    }

    public Player(Player other) {
        this(other.getNickname(), other.getRole());
    }

    public Player(String nickname, Role role) {
        this.nickname = nickname;
        this.role = role;
    }

    public void setAsEncoder() {
        this.role = Role.ENCODER;
    }

    public void setAsDecoder() {
        this.role = Role.DECODER;
    }

    public void setAsInactive() {
        this.role = Role.INACTIVE;
    }

    public String getNickname() {
        return nickname;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(nickname, player.nickname) && role == player.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname, role);
    }

    @Override
    public String toString() {
        return "Player{" +
                "nickname='" + nickname + '\'' +
                ", role=" + role +
                '}';
    }
}
