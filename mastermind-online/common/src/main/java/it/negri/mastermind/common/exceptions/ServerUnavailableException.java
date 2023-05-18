package it.negri.mastermind.common.exceptions;

public class ServerUnavailableException extends Exception {
    public ServerUnavailableException() {

    }

    public ServerUnavailableException(String message) {
        super(message);
    }

    public ServerUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerUnavailableException(Throwable cause) {
        super(cause);
    }
}
