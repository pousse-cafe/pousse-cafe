package poussecafe.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.context.EntityServices;
import poussecafe.context.MetaApplicationContext;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.domain.EntityData;
import poussecafe.domain.EntityImplementation;
import poussecafe.domain.Repository;
import poussecafe.exception.PousseCafeException;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.MessagingConnection;
import poussecafe.messaging.internal.InternalMessagingQueue.InternalMessageReceiver;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class MetaApplicationWrapper {

    public MetaApplicationWrapper(MetaApplicationContext context) {
        Objects.requireNonNull(context);
        this.context = context;
    }

    private MetaApplicationContext context;

    private JsonDataReader jsonDataReader = new JsonDataReader();

    public MetaApplicationContext context() {
        return context;
    }

    @SuppressWarnings("unchecked")
    public <T extends AggregateRoot<K, D>, K, D extends EntityData<K>> T find(Class<T> entityClass,
            K key) {
        waitUntilAllMessageQueuesEmpty();
        Repository<AggregateRoot<K, D>, K, D> repository = (Repository<AggregateRoot<K, D>, K, D>) context
                .getEntityServices(entityClass)
                .getRepository();
        return (T) repository.find(key);
    }

    public void waitUntilAllMessageQueuesEmpty() {
        try {
            for(MessagingConnection connection : context.messagingConnections()) {
                MessageReceiver receiver = connection.messageReceiver();
                if(receiver instanceof InternalMessageReceiver) {
                    InternalMessageReceiver internalMessageReceiver = (InternalMessageReceiver) receiver;
                    internalMessageReceiver.queue().waitUntilEmpty();
                }
            }
        } catch (InterruptedException e) {
            throw new PousseCafeException(e);
        }
    }

    public void addDomainEvent(DomainEvent event) {
        context.getMessageSenderLocator().locate(event.getClass()).sendMessage(event);
        waitUntilAllMessageQueuesEmpty();
    }

    public void loadDataFile(String path) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(getClass().getResource(path).toURI())),
                    StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.reader().readTree(json);
            jsonNode.fieldNames().forEachRemaining(entityClassName -> {
                loadEntity(entityClassName, jsonNode);
            });
        } catch (Exception e) {
            throw new PousseCafeException("Unable to load data file", e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void loadEntity(String entityClassName, JsonNode jsonNode) {
        logger.info("Loading data for entity {}", entityClassName);
        try {
            Class<?> entityClass = Class.forName(entityClassName);
            EntityImplementation entityImplementation = context.environment().getEntityImplementation(entityClass);
            checkThat(value(entityImplementation.getStorage() == InternalStorage.instance()).isTrue());

            EntityServices services = context.getEntityServices(entityClass);
            InternalDataAccess dataAccess = (InternalDataAccess) services.getRepository().dataAccess();
            logger.debug("Field value {}", jsonNode.get(entityClassName));
            jsonNode.get(entityClassName).elements().forEachRemaining(dataJson -> {
                logger.debug("Loading {}", dataJson);
                EntityData<?> dataImplementation = (EntityData<?>) entityImplementation.getDataFactory().get();
                jsonDataReader.readJson(dataImplementation, dataJson);
                dataAccess.addData(dataImplementation);
            });
        } catch (ClassNotFoundException e) {
            throw new PousseCafeException("No entity with class " + entityClassName, e);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());
}
