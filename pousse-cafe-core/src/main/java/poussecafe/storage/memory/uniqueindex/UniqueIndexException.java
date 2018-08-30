package poussecafe.storage.memory.uniqueindex;

import poussecafe.storage.memory.InMemoryDataException;

@SuppressWarnings("serial")
public class UniqueIndexException extends InMemoryDataException {

    public UniqueIndexException() {
    }

    public UniqueIndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueIndexException(String message) {
        super(message);
    }

    public UniqueIndexException(Throwable cause) {
        super(cause);
    }

}
