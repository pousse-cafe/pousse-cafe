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

    List<MessageImplementationConfiguration> implementations;

    public List<MessageImplementationConfiguration> implementations() {
        return Collections.unmodifiableList(implementations);
    }
}
