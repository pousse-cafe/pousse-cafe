package poussecafe.property;

import poussecafe.exception.PousseCafeException;

@SuppressWarnings("serial")
public class AccessPolicyException extends PousseCafeException {

    public AccessPolicyException() {
        super();
    }

    public AccessPolicyException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessPolicyException(String message) {
        super(message);
    }

    public AccessPolicyException(Throwable cause) {
        super(cause);
    }
}
