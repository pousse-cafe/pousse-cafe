package poussecafe.simplemetaapp;

import poussecafe.context.DiscoveredBoundedContext;
import poussecafe.messaging.Messaging;
import poussecafe.messaging.internal.InternalMessaging;
import poussecafe.storage.Storage;
import poussecafe.storage.internal.InternalStorage;

public class MyBoundedContext extends DiscoveredBoundedContext {

    public MyBoundedContext() {
        super("poussecafe.simplemetaapp");
    }

    @Override
    protected Storage storage() {
        return InternalStorage.instance();
    }

    @Override
    protected Messaging messaging() {
        return InternalMessaging.instance();
    }
}
