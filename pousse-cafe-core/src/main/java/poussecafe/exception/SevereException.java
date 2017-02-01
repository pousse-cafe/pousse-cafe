package poussecafe.exception;

public class SevereException extends PousseCafeException {

    private static final long serialVersionUID = 4714887994575550722L;

    public SevereException() {
        super();
    }

    public SevereException(String message, Throwable cause) {
        super(message, cause);
    }

    public SevereException(String message) {
        super(message);
    }

    public SevereException(Throwable cause) {
        super(cause);
    }

}
