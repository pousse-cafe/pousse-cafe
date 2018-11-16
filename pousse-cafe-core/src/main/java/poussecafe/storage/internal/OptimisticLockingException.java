package poussecafe.storage.internal;

@SuppressWarnings("serial")
public class OptimisticLockingException extends InternalStorageException {

    public OptimisticLockingException() {
        super();
    }

    public OptimisticLockingException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptimisticLockingException(String message) {
        super(message);
    }

    public OptimisticLockingException(Throwable cause) {
        super(cause);
    }

}
