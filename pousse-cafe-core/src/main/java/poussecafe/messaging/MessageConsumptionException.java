package poussecafe.messaging;

public class MessageConsumptionException extends RuntimeException {

    private static final long serialVersionUID = -5906618163009616667L;

    public MessageConsumptionException() {
    }

    public MessageConsumptionException(String message) {
        super(message);
    }

    public MessageConsumptionException(Throwable cause) {
        super(cause);
    }

    public MessageConsumptionException(String message, Throwable cause) {
        super(message, cause);
    }

}
