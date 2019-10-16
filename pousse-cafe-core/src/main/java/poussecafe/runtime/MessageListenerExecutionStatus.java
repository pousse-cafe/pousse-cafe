package poussecafe.runtime;


public enum MessageListenerExecutionStatus {
    NOT_EXECUTED,
    SUCCESS,
    IGNORED,
    EXPECTING_RETRY,
    FAILED
}
