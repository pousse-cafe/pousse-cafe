package poussecafe.test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.EntityAttributes;
import poussecafe.environment.NewEntityInstanceSpecification;
import poussecafe.messaging.Message;
import poussecafe.runtime.Command;
import poussecafe.runtime.Runtime;

import static java.util.Collections.emptyList;

public abstract class PousseCafeTest {

    @Before
    @BeforeEach
    public void configureContext() {
        runtime = runtimeBuilder().build();
        runtime.injector().injectDependenciesInto(this);
        runtime.registerListenersOf(this);
        runtime.start();
        wrapper = new TestRuntimeWrapper.Builder()
                .runtime(runtime)
                .maxWaitTime(maxWaitTime())
                .build();
    }

    private Runtime runtime;

    private TestRuntimeWrapper wrapper;

    protected Runtime.Builder runtimeBuilder() {
        return new Runtime.Builder()
                .messageValidation(true);
    }

    public Runtime testRuntime() {
        return wrapper.runtime();
    }

    /**
     * @deprecated inject repository directly in test case class
     */
    @Deprecated(since = "0.18.0")
    public <T extends AggregateRoot<K, D>, K, D extends EntityAttributes<K>> Optional<T> getOptional(Class<T> entityClass,
            K id) {
        return wrapper.getOptional(entityClass, id);
    }

    protected void waitUntilAllMessageQueuesEmpty() {
        wrapper.waitUntilEndOfMessageProcessing();
    }

    protected Optional<Duration> maxWaitTime() {
        return Optional.of(Duration.ofSeconds(5));
    }

    public <D extends DomainEvent> D newDomainEvent(Class<D> eventClass) {
        return wrapper.runtime().environment().messageFactory().newMessage(eventClass);
    }

    /**
     * @deprecated use issue(DomainEvent) instead.
     */
    @Deprecated(since = "0.17")
    public void emitDomainEvent(DomainEvent event) {
        issue(event);
    }

    public void issue(DomainEvent event) {
        wrapper.issue(event);
    }

    /**
     * @deprecated use issue(List<? extends DomainEvent>) instead.
     */
    @Deprecated(since = "0.17")
    public void emitDomainEvents(List<? extends DomainEvent> event) {
        issue(event);
    }

    public void issue(List<? extends DomainEvent> events) {
        wrapper.issue(events);
    }

    /**
     * @deprecated use {@link #given(DataSet)} instead (loaded JSON file has to be converted into a DataSet instance).
     */
    @Deprecated(since = "0.28")
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <E> E newEntity(Class<E> entityClass) {
        return (E) testRuntime().environment().entityFactory().newEntity(new NewEntityInstanceSpecification.Builder()
                    .entityClass(entityClass)
                    .instantiateData(true)
                    .build());
    }

    /**
     * @deprecated use {@link #given(DataSet)} instead (loaded JSON file has to be converted into a DataSet instance).
     */
    @Deprecated(since = "0.28")
    public void given(String resourceName) {
        loadDataFile("/" + resourceName + ".json");
    }

    public void when(Command command) {
        submitCommand(command);
    }

    public void when(DomainEvent event) {
        issue(event);
    }

    public void whenCommands(List<? extends Command> commands) {
        wrapper.submitCommands(commands);
    }

    public void whenEvents(List<? extends DomainEvent> events) {
        issue(events);
    }

    @MessageListener
    @SuppressWarnings("unchecked")
    public synchronized void catchAll(Message message) {
        Class<? extends Message> messageClass = runtime.environment().definedMessageClass(message.getClass());
        var messages = issuedMessages.computeIfAbsent(messageClass, key -> new ArrayList<>());
        messages.add(message);
    }

    @SuppressWarnings("rawtypes")
    private Map<Class<? extends Message>, List> issuedMessages = new HashMap<>();

    @SuppressWarnings("unchecked")
    public synchronized <M extends Message> List<M> capturedMessages(Class<M> messageClass) {
        return issuedMessages.getOrDefault(messageClass, emptyList());
    }

    public <M extends Message> M capturedMessage(Class<M> messageClass) {
        var consumed = capturedMessages(messageClass);
        if(consumed.size() != 1) {
            throw new IllegalStateException(consumed.size() + " messages were captured instead of a single one");
        } else {
            return consumed.get(0);
        }
    }

    @SuppressWarnings("rawtypes")
    public void given(DataSet dataSet) {
        for(Entry<Class, List<EntityAttributes>> data : dataSet.data().entrySet()) {
            wrapper.loadEntity(data.getKey(), data.getValue());
        }
    }
}
