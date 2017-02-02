package poussecafe.test;

import java.time.Duration;
import poussecafe.configuration.MetaApplicationContext;
import poussecafe.consequence.Command;
import poussecafe.consequence.CommandHandlingResult;
import poussecafe.storable.Storable;
import poussecafe.storable.StorableData;
import poussecafe.storable.StorableRepository;

import static org.junit.Assert.assertTrue;

public class WorkflowTest {

    protected TestMetaApplicationConfiguration configuration;

    private MetaApplicationContext context;

    protected void givenContext() {
        configuration = new TestMetaApplicationConfiguration();
        registerActors();
        context = new MetaApplicationContext(configuration);
    }

    protected void registerActors() {

    }

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
        try {
            configuration.waitUntilAllConsequenceQueuesEmpty();
        } catch (InterruptedException e) {
            return null;
        }
        StorableRepository<Storable<K, D>, K, D> repository = (StorableRepository<Storable<K, D>, K, D>) context
                .getStorableServices(storableClass)
                .getRepository();
        return (T) repository.find(key);
    }
}
