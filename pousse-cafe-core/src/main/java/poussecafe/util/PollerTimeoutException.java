package poussecafe.util;

import poussecafe.exception.PousseCafeException;

@SuppressWarnings("serial")
public class PollerTimeoutException extends PousseCafeException {

    public PollerTimeoutException() {
    }

    public PollerTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public PollerTimeoutException(String message) {
        super(message);
    }

    public PollerTimeoutException(Throwable cause) {
        super(cause);
    }

}
