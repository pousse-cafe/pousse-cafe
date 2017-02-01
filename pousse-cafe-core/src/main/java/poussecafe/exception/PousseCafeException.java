package poussecafe.exception;

public class PousseCafeException extends RuntimeException {

    private static final long serialVersionUID = 7029365620965007009L;

    public PousseCafeException() {
        super();
    }

    public PousseCafeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PousseCafeException(String message) {
        super(message);
    }

    public PousseCafeException(Throwable cause) {
        super(cause);
    }

}
