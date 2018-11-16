package poussecafe.simplemetaapp;

import poussecafe.context.DiscoveredBoundedContext;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.internal.InternalMessaging;
import poussecafe.storage.Storage;
import poussecafe.storage.memory.InMemoryStorage;

public class MyBoundedContext extends DiscoveredBoundedContext {

    public MyBoundedContext() {
        super("poussecafe.simplemetaapp");
    }

    @Override
    protected Storage storage() {
        return InMemoryStorage.instance();
    }

    @Override
    protected Messaging messaging() {
        return InternalMessaging.instance();
    }
}
