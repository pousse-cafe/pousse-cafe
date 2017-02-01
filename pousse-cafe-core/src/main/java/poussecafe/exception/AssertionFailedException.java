package poussecafe.exception;

public class AssertionFailedException extends SevereException {

    private static final long serialVersionUID = -4183429913220074054L;

    public AssertionFailedException() {
        super();
    }

    public AssertionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssertionFailedException(String message) {
        super(message);
    }

    public AssertionFailedException(Throwable cause) {
        super(cause);
    }

}
