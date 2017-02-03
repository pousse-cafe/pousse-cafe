package poussecafe.test;

import java.time.Duration;
import org.junit.Before;
import poussecafe.configuration.MetaApplicationContext;
import poussecafe.consequence.Command;
import poussecafe.consequence.CommandHandlingResult;
import poussecafe.domain.DomainEvent;
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

    protected void addDomainEvent(DomainEvent event) {
        context.getConsequenceRouter().routeConsequence(event);
        waitUntilAllConsequenceQueuesEmpty();
    }
}
