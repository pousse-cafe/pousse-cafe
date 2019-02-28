package poussecafe.util;

import poussecafe.exception.PousseCafeException;

@SuppressWarnings("serial")
public class ReflectionException extends PousseCafeException {

    public ReflectionException() {
        super();
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }
}
