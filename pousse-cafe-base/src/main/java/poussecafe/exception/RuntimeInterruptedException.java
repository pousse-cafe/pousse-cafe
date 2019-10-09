package poussecafe.exception;

@SuppressWarnings("serial")
public class RuntimeInterruptedException extends RuntimeException {

    public RuntimeInterruptedException() {
        super();
    }

    public RuntimeInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeInterruptedException(String message) {
        super(message);
    }

    public RuntimeInterruptedException(Throwable cause) {
        super(cause);
    }
}
