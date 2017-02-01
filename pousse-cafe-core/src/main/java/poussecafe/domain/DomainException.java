package poussecafe.domain;

public class DomainException extends RuntimeException {

    private static final long serialVersionUID = 8219794829692291711L;

    public DomainException() {
    }

    public DomainException(String message) {
        super(message);
    }

    public DomainException(Throwable cause) {
        super(cause);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

}
