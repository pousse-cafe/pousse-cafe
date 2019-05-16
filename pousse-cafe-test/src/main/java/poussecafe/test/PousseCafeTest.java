package poussecafe.test;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.EntityAttributes;
import poussecafe.environment.BoundedContext;
import poussecafe.runtime.Command;
import poussecafe.runtime.Runtime;

public abstract class PousseCafeTest {

    private RuntimeWrapper wrapper;

    @Before
    public void configureContext() {
        Runtime context = new Runtime.Builder()
            .failFast(true)
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

    public <T extends AggregateRoot<K, D>, K, D extends EntityAttributes<K>> T find(Class<T> entityClass,
            K id) {
        return wrapper.find(entityClass, id);
    }

    protected void waitUntilAllMessageQueuesEmpty() {
        wrapper.waitUntilEndOfMessageProcessing();
    }

    protected <D extends DomainEvent> D newDomainEvent(Class<D> eventClass) {
        return wrapper.runtime().environment().messageFactory().newMessage(eventClass);
    }

    public void emitDomainEvent(DomainEvent event) {
        wrapper.emitDomainEvent(event);
    }

    public void loadDataFile(String path) {
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

    public <C extends Command> C newCommand(Class<C> commandClass) {
        return wrapper.runtime().newCommand(commandClass);
    }

    public void submitCommand(Command command) {
        wrapper.submitCommand(command);
    }

    public PousseCafeTestObjectMapper objectMapper() {
        return wrapper.objectMapper();
    }
}
