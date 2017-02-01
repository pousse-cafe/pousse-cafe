package poussecafe.consequence;

public class ConsequenceConsumptionException extends RuntimeException {

    private static final long serialVersionUID = -5906618163009616667L;

    public ConsequenceConsumptionException() {
    }

    public ConsequenceConsumptionException(String message) {
        super(message);
    }

    public ConsequenceConsumptionException(Throwable cause) {
        super(cause);
    }

    public ConsequenceConsumptionException(String message, Throwable cause) {
        super(message, cause);
    }

}
