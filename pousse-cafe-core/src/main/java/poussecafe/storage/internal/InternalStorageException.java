package poussecafe.storage.internal;

import poussecafe.exception.PousseCafeException;

@SuppressWarnings("serial")
public class InternalStorageException extends PousseCafeException {

    public InternalStorageException() {
        super();
    }

    public InternalStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public InternalStorageException(String message) {
        super(message);
    }

    public InternalStorageException(Throwable cause) {
        super(cause);
    }

}
