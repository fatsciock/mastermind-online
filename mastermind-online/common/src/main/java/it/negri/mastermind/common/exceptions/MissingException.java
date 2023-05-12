package it.negri.mastermind.common.exceptions;

public class MissingException extends Exception {
    public MissingException() {

    }

    public MissingException(String message) {
        super(message);
    }

    public MissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingException(Throwable cause) {
        super(cause);
    }
}
