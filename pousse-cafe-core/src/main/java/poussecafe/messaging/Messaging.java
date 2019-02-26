package poussecafe.messaging;

import poussecafe.runtime.MessageConsumer;

public abstract class Messaging {

    public abstract String name();

    public boolean nameIn(String[] array) {
        for(String messageName : array) {
            if(name().equals(messageName)) {
                return true;
            }
        }
        return false;
    }

    public abstract MessagingConnection connect(MessageConsumer messageConsumer);
}
