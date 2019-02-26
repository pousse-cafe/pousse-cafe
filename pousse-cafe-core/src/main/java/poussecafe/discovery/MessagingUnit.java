package poussecafe.discovery;

import java.util.Collections;
import java.util.List;
import poussecafe.messaging.MessageImplementation;
import poussecafe.messaging.Messaging;

public class MessagingUnit {

    MessagingUnit() {

    }

    Messaging messaging;

    public Messaging messaging() {
        return messaging;
    }

    List<MessageImplementation> implementations;

    public List<MessageImplementation> implementations() {
        return Collections.unmodifiableList(implementations);
    }
}
