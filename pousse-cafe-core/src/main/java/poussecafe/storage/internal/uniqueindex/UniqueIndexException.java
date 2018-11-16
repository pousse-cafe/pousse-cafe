package poussecafe.storage.internal.uniqueindex;

import poussecafe.storage.internal.InternalStorageException;

@SuppressWarnings("serial")
public class UniqueIndexException extends InternalStorageException {

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
