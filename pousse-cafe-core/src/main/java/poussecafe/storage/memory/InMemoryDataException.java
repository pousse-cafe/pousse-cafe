package poussecafe.storage.memory;

import poussecafe.exception.PousseCafeException;

public class InMemoryDataException extends PousseCafeException {

    private static final long serialVersionUID = -8785908098208532749L;

    public InMemoryDataException() {
        super();
    }

    public InMemoryDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InMemoryDataException(String message) {
        super(message);
    }

    public InMemoryDataException(Throwable cause) {
        super(cause);
    }

}
