package it.negri.mastermind.common.exceptions;

public class PlayerTimeoutException extends Exception {
    public PlayerTimeoutException() {
    }

    public PlayerTimeoutException(String message) {
        super(message);
    }

    public PlayerTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerTimeoutException(Throwable cause) {
        super(cause);
    }
}