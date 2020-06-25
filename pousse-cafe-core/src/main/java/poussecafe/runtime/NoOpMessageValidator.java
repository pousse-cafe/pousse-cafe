package poussecafe.runtime;

import poussecafe.messaging.Message;

public class NoOpMessageValidator implements MessageValidator {

    @Override
    public void validOrElseThrow(Message message) {
        // No validation
    }

    public static NoOpMessageValidator instance() {
        return SINGLETON;
    }

    private static final NoOpMessageValidator SINGLETON = new NoOpMessageValidator();

    private NoOpMessageValidator() {

    }
}
