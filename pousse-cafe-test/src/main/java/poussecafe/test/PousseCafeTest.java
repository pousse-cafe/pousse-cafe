package poussecafe.test;

import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.EntityAttributes;
import poussecafe.environment.BoundedContext;
import poussecafe.runtime.Command;
import poussecafe.runtime.Runtime;

public abstract class PousseCafeTest {

    private TestRuntimeWrapper wrapper;

    @Before
    public void configureContext() {
        Runtime runtime = new Runtime.Builder()
            .failFast(true)
            .withBoundedContexts(boundedContexts())
            .processingThreads(2)
            .build();
        runtime.injector().injectDependenciesInto(this);
        runtime.registerListenersOf(this);
        runtime.start();
        wrapper = new TestRuntimeWrapper(runtime);
    }

    protected abstract List<BoundedContext> boundedContexts();

    protected Runtime testRuntime() {
        return wrapper.runtime();
    }

    /**
     * @deprecated use getOptional instead
     */
    @Deprecated(since = "0.8.0")
    public <T extends AggregateRoot<K, D>, K, D extends EntityAttributes<K>> T find(Class<T> entityClass,
            K id) {
        return wrapper.find(entityClass, id);
    }

    public <T extends AggregateRoot<K, D>, K, D extends EntityAttributes<K>> Optional<T> getOptional(Class<T> entityClass,
            K id) {
        return wrapper.getOptional(entityClass, id);
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

    protected TestRuntimeWrapper wrapper() {
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
