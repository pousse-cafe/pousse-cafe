package poussecafe.messaging;


@SuppressWarnings("serial")
public class UnknownMessageTypeException extends RuntimeException {

    public UnknownMessageTypeException() {

    }

    public UnknownMessageTypeException(String message) {
        super(message);
    }

    public UnknownMessageTypeException(Throwable cause) {
        super(cause);
    }

    public UnknownMessageTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
