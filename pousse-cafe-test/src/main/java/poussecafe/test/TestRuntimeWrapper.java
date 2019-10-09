package poussecafe.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    public TestRuntimeWrapper(Runtime context) {
        Objects.requireNonNull(context);
        runtime = context;
        runtimeFriend = new RuntimeFriend(runtime);
    }

    private Runtime runtime;

    private RuntimeFriend runtimeFriend;

    private PousseCafeTestObjectMapper objectMapper = new PousseCafeTestObjectMapper();

    public Runtime runtime() {
        return runtime;
    }

    /**
     * @deprecated use getOptional instead
     */
    @SuppressWarnings("unchecked")
    @Deprecated(since = "0.8.0")
    public <T extends AggregateRoot<K, D>, K, D extends EntityAttributes<K>> T find(Class<T> entityClass,
            K id) {
        waitUntilEndOfMessageProcessing();
        Repository<AggregateRoot<K, D>, K, D> repository = (Repository<AggregateRoot<K, D>, K, D>) runtime
                .environment()
                .repositoryOf(entityClass)
                .orElseThrow(PousseCafeException::new);
        return (T) repository.find(id);
    }

    @SuppressWarnings("unchecked")
    public <T extends AggregateRoot<K, D>, K, D extends EntityAttributes<K>> Optional<T> getOptional(Class<T> entityClass,
            K id) {
        waitUntilEndOfMessageProcessing();
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
                internalMessageReceiver.queue().waitUntilEmptyOrInterrupted();
            }
        }
    }

    public void emitDomainEvent(DomainEvent event) {
        runtimeFriend.messageSenderLocator().locate(event.getClass()).sendMessage(event);
        waitUntilEndOfMessageProcessing();
    }

    public void emitDomainEvents(List<? extends DomainEvent> events) {
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
            JsonNode jsonNode = mapper.reader().readTree(json);
            jsonNode.fieldNames().forEachRemaining(entityClassName -> loadEntity(entityClassName, jsonNode));
        } catch (Exception e) {
            throw new PousseCafeException("Unable to load data file", e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void loadEntity(String entityClassName, JsonNode jsonNode) {
        logger.info("Loading data for entity {}", entityClassName);
        try {
            Class<?> entityClass = Class.forName(entityClassName);
            EntityImplementation entityImplementation = runtime.environment().entityImplementation(entityClass);
            if(entityImplementation.getStorage() != InternalStorage.instance()) {
                throw new PousseCafeException("Unsupported test storage");
            }

            AggregateServices services = runtime.environment().aggregateServicesOf(entityClass).orElseThrow(PousseCafeException::new);
            InternalDataAccess dataAccess = (InternalDataAccess) services.repository().dataAccess();
            logger.debug("Field value {}", jsonNode.get(entityClassName));
            jsonNode.get(entityClassName).elements().forEachRemaining(dataJson -> {
                logger.debug("Loading {}", dataJson);
                EntityAttributes<?> dataImplementation = (EntityAttributes<?>) entityImplementation.getDataFactory().get();
                objectMapper.readJson(dataImplementation, dataJson);
                dataAccess.addData(dataImplementation);
            });
        } catch (ClassNotFoundException e) {
            throw new PousseCafeException("No entity with class " + entityClassName, e);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void submitCommand(Command command) {
        runtime.submitCommand(command);
        waitUntilEndOfMessageProcessing();
    }

    public PousseCafeTestObjectMapper objectMapper() {
        return objectMapper;
    }
}
