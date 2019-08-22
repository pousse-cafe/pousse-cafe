package poussecafe.runtime;

import poussecafe.exception.PousseCafeException;

@SuppressWarnings("serial")
public class FailFastException extends PousseCafeException {

    public FailFastException() {
        super();
    }

    public FailFastException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailFastException(String message) {
        super(message);
    }

    public FailFastException(Throwable cause) {
        super(cause);
    }
}
