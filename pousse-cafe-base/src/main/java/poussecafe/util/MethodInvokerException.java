package poussecafe.util;

@SuppressWarnings("serial")
public class MethodInvokerException extends RuntimeException {

    public MethodInvokerException() {
        super();
    }

    public MethodInvokerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodInvokerException(String message) {
        super(message);
    }

    public MethodInvokerException(Throwable cause) {
        super(cause);
    }
}
