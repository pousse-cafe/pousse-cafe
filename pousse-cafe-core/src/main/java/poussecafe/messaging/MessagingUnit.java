package poussecafe.messaging;

import java.util.Collections;
import java.util.List;

public class MessagingUnit {

    MessagingUnit() {

    }

    Messaging messaging;

    public Messaging messaging() {
        return messaging;
    }

    List<poussecafe.messaging.MessageImplementationConfiguration> implementations;

    public List<poussecafe.messaging.MessageImplementationConfiguration> implementations() {
        return Collections.unmodifiableList(implementations);
    }
}
