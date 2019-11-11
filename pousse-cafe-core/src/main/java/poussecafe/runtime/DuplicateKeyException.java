package poussecafe.runtime;

import poussecafe.exception.PousseCafeException;

@SuppressWarnings("serial")
public class DuplicateKeyException extends PousseCafeException {

    public DuplicateKeyException() {
        super();
    }

    public DuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateKeyException(String message) {
        super(message);
    }

    public DuplicateKeyException(Throwable cause) {
        super(cause);
    }

}
