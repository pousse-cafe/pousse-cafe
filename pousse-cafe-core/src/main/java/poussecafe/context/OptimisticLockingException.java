package poussecafe.context;

import poussecafe.exception.PousseCafeException;

@SuppressWarnings("serial")
public class OptimisticLockingException extends PousseCafeException {

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
