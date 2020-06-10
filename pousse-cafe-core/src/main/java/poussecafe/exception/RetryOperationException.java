package poussecafe.exception;

@SuppressWarnings("serial")
public class RetryOperationException extends PousseCafeException {

    public RetryOperationException() {
        super();
    }

    public RetryOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryOperationException(String message) {
        super(message);
    }

    public RetryOperationException(Throwable cause) {
        super(cause);
    }

}
