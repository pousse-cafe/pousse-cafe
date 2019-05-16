package poussecafe.messaging;

@SuppressWarnings("serial")
public class MessageAdapterException extends RuntimeException {

    public MessageAdapterException() {
        super();
    }

    public MessageAdapterException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageAdapterException(String message) {
        super(message);
    }

    public MessageAdapterException(Throwable cause) {
        super(cause);
    }
}
