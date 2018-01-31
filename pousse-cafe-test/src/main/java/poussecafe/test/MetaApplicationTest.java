package poussecafe.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Before;
import poussecafe.context.MetaApplicationContext;
import poussecafe.domain.DomainEvent;
import poussecafe.exception.PousseCafeException;
import poussecafe.storable.IdentifiedStorable;
import poussecafe.storable.IdentifiedStorableRepository;
import poussecafe.storable.StorableData;

public abstract class MetaApplicationTest {

    private MetaApplicationContext context;

    @Before
    public void configureContext() {
        context = new MetaApplicationContext();
        registerComponents();
        context.start();
    }

    protected abstract void registerComponents();

    protected MetaApplicationContext context() {
        return context;
    }

    @SuppressWarnings("unchecked")
    protected <T extends IdentifiedStorable<K, D>, K, D extends StorableData> T find(Class<T> storableClass,
            K key) {
        waitUntilAllMessageQueuesEmpty();
        IdentifiedStorableRepository<IdentifiedStorable<K, D>, K, D> repository = (IdentifiedStorableRepository<IdentifiedStorable<K, D>, K, D>) context
                .getStorableServices(storableClass)
                .getRepository();
        return (T) repository.find(key);
    }

    protected void waitUntilAllMessageQueuesEmpty() {
        try {
            context.getInMemoryQueue().waitUntilEmpty();
        } catch (InterruptedException e) {
            return;
        }
    }

    protected void addDomainEvent(DomainEvent event) {
        context.getMessageSender().sendMessage(event);
        waitUntilAllMessageQueuesEmpty();
    }

    protected void loadDataFile(String path) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(getClass().getResource(path).toURI())),
                    Charset.forName("UTF-8"));

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.reader().readTree(json);
            jsonNode.fieldNames().forEachRemaining(field -> {
                // TODO
            });
        } catch (Exception e) {
            throw new PousseCafeException("Unable to load data file", e);
        }
    }
}
