package poussecafe.test;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.EntityAttributes;
import poussecafe.environment.BoundedContext;
import poussecafe.runtime.Runtime;

public abstract class PousseCafeTest {

    private RuntimeWrapper wrapper;

    @Before
    public void configureContext() {
        Runtime context = new Runtime.Builder()
            .withBoundedContexts(boundedContexts())
            .build();
        context.injector().injectDependenciesInto(this);
        context.registerListenersOf(this);
        context.start();
        wrapper = new RuntimeWrapper(context);
    }

    protected abstract List<BoundedContext> boundedContexts();

    protected Runtime context() {
        return wrapper.runtime();
    }

    protected <T extends AggregateRoot<K, D>, K, D extends EntityAttributes<K>> T find(Class<T> entityClass,
            K key) {
        return wrapper.find(entityClass, key);
    }

    protected void waitUntilAllMessageQueuesEmpty() {
        wrapper.waitUntilAllMessageQueuesEmpty();
    }

    protected <D extends DomainEvent> D newDomainEvent(Class<D> eventClass) {
        return wrapper.runtime().environment().messageFactory().newMessage(eventClass);
    }

    protected void addDomainEvent(DomainEvent event) {
        wrapper.addDomainEvent(event);
    }

    protected void loadDataFile(String path) {
        wrapper.loadDataFile(path);
    }

    protected RuntimeWrapper wrapper() {
        return wrapper;
    }

    @After
    public void tearDownInternalMessaging() {
        if(wrapper != null) {
            wrapper.runtime().stop();
        }
    }
}
