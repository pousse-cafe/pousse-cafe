package poussecafe.test;

import java.time.Duration;
import poussecafe.configuration.ApplicationContext;
import poussecafe.consequence.Command;
import poussecafe.consequence.CommandHandlingResult;
import poussecafe.storable.Storable;
import poussecafe.storable.StorableData;
import poussecafe.storable.StorableRepository;

import static org.junit.Assert.assertTrue;

public class WorkflowTest {

    protected TestApplicationConfiguration configuration;

    private ApplicationContext context;

    protected void givenContext() {
        configuration = new TestApplicationConfiguration();
        registerActors();
        context = new ApplicationContext(configuration);
    }

    protected void registerActors() {

    }

    protected ApplicationContext context() {
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
    protected <T extends Storable<K, D>, K, D extends StorableData<K>> T getEventually(Class<T> storableClass,
            K key) {
        StorableRepository<Storable<K, D>, K, D> repository = (StorableRepository<Storable<K, D>, K, D>) context
                .getStorableServices(storableClass)
                .getRepository();
        T order = null;
        for (int i = 0; i < 10; ++i) {
            order = (T) repository.find(key);
            if (order != null) {
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return order;
    }
}
