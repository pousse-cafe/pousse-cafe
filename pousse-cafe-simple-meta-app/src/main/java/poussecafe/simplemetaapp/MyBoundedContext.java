package poussecafe.simplemetaapp;

import poussecafe.context.DiscoveredBoundedContext;
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
}
