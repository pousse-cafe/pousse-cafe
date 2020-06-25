package poussecafe.runtime;

import poussecafe.messaging.Message;

public interface MessageValidator {

    void validOrThrow(Message message);
}
