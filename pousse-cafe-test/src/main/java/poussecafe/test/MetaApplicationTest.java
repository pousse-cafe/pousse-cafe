package poussecafe.test;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import poussecafe.context.BoundedContext;
import poussecafe.context.MetaApplicationContext;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.EntityAttributes;

public abstract class MetaApplicationTest {

    private MetaApplicationWrapper wrapper;

    @Before
    public void configureContext() {
        MetaApplicationContext context = new MetaApplicationContext();
        for(BoundedContext bundle : testBundle()) {
            context.addBoundedContext(bundle);
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

    protected <T extends AggregateRoot<K, D>, K, D extends EntityAttributes<K>> T find(Class<T> entityClass,
            K key) {
        return wrapper.find(entityClass, key);
    }

    protected void waitUntilAllMessageQueuesEmpty() {
        wrapper.waitUntilAllMessageQueuesEmpty();
    }

    protected <D extends DomainEvent> D newDomainEvent(Class<D> eventClass) {
        return wrapper.context().getComponentFactory().newMessage(eventClass);
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

    @After
    public void tearDownInternalMessaging() {
        if(wrapper != null) {
            wrapper.context().stopMessageHandling();
        }
    }
}
