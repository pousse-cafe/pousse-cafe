package poussecafe.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.EntityAttributes;
import poussecafe.domain.Repository;
import poussecafe.environment.AggregateServices;
import poussecafe.environment.EntityImplementation;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessagingConnection;
import poussecafe.messaging.internal.InternalMessagingQueue.InternalMessageReceiver;
import poussecafe.runtime.Command;
import poussecafe.runtime.Runtime;
import poussecafe.runtime.RuntimeFriend;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

public class TestRuntimeWrapper {

    public static class Builder {

        private TestRuntimeWrapper wrapper = new TestRuntimeWrapper();

        public TestRuntimeWrapper build() {
            Objects.requireNonNull(wrapper.runtime);
            Objects.requireNonNull(wrapper.maxWaitTime);

            wrapper.runtimeFriend = new RuntimeFriend(wrapper.runtime);

            return wrapper;
        }

        public Builder runtime(Runtime runtime) {
            wrapper.runtime = runtime;
            return this;
        }

        public Builder maxWaitTime(Optional<Duration> maxWaitTime) {
            wrapper.maxWaitTime = maxWaitTime;
            return this;
        }
    }

    private TestRuntimeWrapper() {

    }

    private Runtime runtime;

    private RuntimeFriend runtimeFriend;

    private PousseCafeTestObjectMapper objectMapper = new PousseCafeTestObjectMapper();

    public Runtime runtime() {
        return runtime;
    }

    private Optional<Duration> maxWaitTime = Optional.of(Duration.ofSeconds(5));

    /**
     * @deprecated inject repository directly in test case class
     */
    @SuppressWarnings("unchecked")
    @Deprecated(since = "0.18.0")
    public <T extends AggregateRoot<K, D>, K, D extends EntityAttributes<K>> Optional<T> getOptional(Class<T> entityClass,
            K id) {
        Repository<AggregateRoot<K, D>, K, D> repository = (Repository<AggregateRoot<K, D>, K, D>) runtime
                .environment()
                .repositoryOf(entityClass)
                .orElseThrow(PousseCafeException::new);
        return (Optional<T>) repository.getOptional(id);
    }

    public void waitUntilEndOfMessageProcessing() {
        for(MessagingConnection connection : runtime.messagingConnections()) {
            @SuppressWarnings("rawtypes")
            MessageReceiver receiver = connection.messageReceiver();
            if(receiver instanceof InternalMessageReceiver) {
                InternalMessageReceiver internalMessageReceiver = (InternalMessageReceiver) receiver;
                internalMessageReceiver.queue().waitUntilEmptyOrInterrupted(Duration.ofMillis(100), maxWaitTime);
            }
        }
    }

    public void issue(DomainEvent event) {
        runtimeFriend.messageSenderLocator().locate(event.getClass()).sendMessage(event);
        waitUntilEndOfMessageProcessing();
    }

    public void issue(List<? extends DomainEvent> events) {
        for(DomainEvent event : events) {
            runtimeFriend.messageSenderLocator().locate(event.getClass()).sendMessage(event);
        }
        waitUntilEndOfMessageProcessing();
    }

    public void loadDataFile(String resourceName) {
        URL fileUrl = getClass().getResource(resourceName);
        if(fileUrl == null) {
            throw new PousseCafeException("Resource " + resourceName + " does not exist");
        }

        try {
            String json = new String(Files.readAllBytes(Paths.get(fileUrl.toURI())),
                    StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode tree = mapper.reader().readTree(json);
            tree.fieldNames().forEachRemaining(name -> loadEntity(name, tree));
        } catch (Exception e) {
            throw new PousseCafeException("Unable to load data file", e);
        }
    }

    @SuppressWarnings({ "rawtypes" })
    private void loadEntity(String name, JsonNode tree) {
        logger.info("Loading data for entity {}", name);
        Optional<EntityImplementation> optionalEntityImplementation =
                runtime.environment().entityImplementationByName(name);
        if(optionalEntityImplementation.isEmpty()) {
            throw new PousseCafeException("No entity implementation found with name " + name);
        }

        var entityImplementation = optionalEntityImplementation.get();
        if(entityImplementation.getStorage() != InternalStorage.instance()) {
            throw new PousseCafeException("Unsupported test storage");
        }

        AggregateServices services = runtime.environment().aggregateServicesOf(entityImplementation.getEntityClass())
                .orElseThrow();
        InternalDataAccess dataAccess = (InternalDataAccess) services.repository().dataAccess();
        var entities = tree.get(name).elements();
        var loader = new EntityLoader.Builder()
                .entityImplementation(entityImplementation)
                .dataAccess(dataAccess)
                .objectMapper(objectMapper)
                .build();
        entities.forEachRemaining(loader::loadEntity);
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void submitCommand(Command command) {
        try {
            submitAndWaitProcessed(command);
            waitUntilEndOfMessageProcessing();
        } catch (Exception e) {
            throw new PousseCafeException("Error while submitting command", e);
        }
    }

    private void submitAndWaitProcessed(Command command) throws InterruptedException, ExecutionException, TimeoutException {
        if(maxWaitTime.isPresent()) {
            runtime.submitCommand(command).get(maxWaitTime.get().toSeconds(), TimeUnit.SECONDS);
        } else {
            runtime.submitCommand(command).get();
        }
    }

    public void submitCommands(List<? extends Command> commands) {
        try {
            for(Command command : commands) {
                submitAndWaitProcessed(command);
            }
            waitUntilEndOfMessageProcessing();
        } catch (Exception e) {
            throw new PousseCafeException("Error while submitting command", e);
        }
    }

    public PousseCafeTestObjectMapper objectMapper() {
        return objectMapper;
    }
}
