package poussecafe.test;

import java.util.List;
import org.junit.Before;
import poussecafe.context.BoundedContext;
import poussecafe.context.MetaApplicationContext;
import poussecafe.domain.DomainEvent;
import poussecafe.storable.IdentifiedStorable;
import poussecafe.storable.IdentifiedStorableData;

public abstract class MetaApplicationTest {

    private MetaApplicationWrapper wrapper;

    @Before
    public void configureContext() {
        MetaApplicationContext context = new MetaApplicationContext();
        for(BoundedContext bundle : testBundle()) {
            context.loadBoundedContext(bundle);
        }
        context.load();
        context.injectDependencies(this);
        context.registerListeners(this);
        context.startMessageHandling();
        wrapper = new MetaApplicationWrapper(context);
    }

    protected abstract List<BoundedContext> testBundle();

    protected MetaApplicationContext context() {
        return wrapper.context();
    }

    protected <T extends IdentifiedStorable<K, D>, K, D extends IdentifiedStorableData<K>> T find(Class<T> storableClass,
            K key) {
        return wrapper.find(storableClass, key);
    }

    protected void waitUntilAllMessageQueuesEmpty() {
        wrapper.waitUntilAllMessageQueuesEmpty();
    }

    protected void addDomainEvent(DomainEvent event) {
        wrapper.addDomainEvent(event);
    }

    protected void loadDataFile(String path) {
        wrapper.loadDataFile(path);
    }

    protected MetaApplicationWrapper wrapper() {
        return wrapper;
    }
}
