package poussecafe.source.analysis;

@SuppressWarnings("serial")
public class ResolutionException extends RuntimeException {

    public ResolutionException() {
    }

    public ResolutionException(String message) {
        super(message);
    }

    public ResolutionException(Throwable cause) {
        super(cause);
    }

    public ResolutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
