package poussecafe.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map.Entry;
import org.junit.Before;
import poussecafe.configuration.MetaApplicationContext;
import poussecafe.consequence.Command;
import poussecafe.consequence.CommandHandlingResult;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.domain.DomainEvent;
import poussecafe.exception.PousseCafeException;
import poussecafe.storable.Storable;
import poussecafe.storable.StorableData;
import poussecafe.storable.StorableRepository;

import static org.junit.Assert.assertTrue;

public abstract class MetaApplicationTest {

    protected TestMetaApplicationConfiguration configuration;

    private MetaApplicationContext context;

    @Before
    public void configureContext() {
        configuration = new TestMetaApplicationConfiguration();
        registerComponents();
        context = new MetaApplicationContext(configuration);
    }

    protected abstract void registerComponents();

    protected MetaApplicationContext context() {
        return context;
    }

    protected void processAndAssertSuccess(Command command) {
        CommandHandlingResult result = context()
                .getCommandProcessor()
                .processCommand(command)
                .get(Duration.ofSeconds(1));
        assertTrue(result.toString(), result.isSuccess());
    }

    @SuppressWarnings("unchecked")
    protected <T extends Storable<K, D>, K, D extends StorableData<K>> T find(Class<T> storableClass,
            K key) {
        waitUntilAllConsequenceQueuesEmpty();
        StorableRepository<Storable<K, D>, K, D> repository = (StorableRepository<Storable<K, D>, K, D>) context
                .getStorableServices(storableClass)
                .getRepository();
        return (T) repository.find(key);
    }

    protected void waitUntilAllConsequenceQueuesEmpty() {
        try {
            configuration.waitUntilAllConsequenceQueuesEmpty();
        } catch (InterruptedException e) {
            return;
        }
    }

    @SuppressWarnings("rawtypes")
    private StorableRepository getRepository(Class storableClass) {
        return context.getStorableServices(storableClass).getRepository();
    }

    protected void addDomainEvent(DomainEvent event) {
        context.getConsequenceRouter().routeConsequence(event);
        waitUntilAllConsequenceQueuesEmpty();
    }

    @SuppressWarnings("rawtypes")
    protected void loadDataFile(String path) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(getClass().getResource(path).toURI())),
                    Charset.forName("UTF-8"));
            Gson gson = new GsonBuilder().create();
            JsonObject root = gson.fromJson(json, JsonObject.class);
            for (Entry<String, JsonElement> entry : root.entrySet()) {
                Class<?> storableClass = Class.forName(entry.getKey());
                StorableRepository repository = getRepository(storableClass);
                InMemoryDataAccess dataAccess = (InMemoryDataAccess) repository.getDataAccess();
                JsonArray dataArray = (JsonArray) entry.getValue();
                dataArray.forEach(element -> {
                    dataAccess.addRawData(element);
                });
            }
        } catch (Exception e) {
            throw new PousseCafeException("Unable to load data file", e);
        }
    }
}
