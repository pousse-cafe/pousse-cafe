package poussecafe.exception;

@SuppressWarnings("serial")
public class SameOperationException extends PousseCafeException {

    public SameOperationException() {
        super();
    }

    public SameOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SameOperationException(String message) {
        super(message);
    }

    public SameOperationException(Throwable cause) {
        super(cause);
    }

}
