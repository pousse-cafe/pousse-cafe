package workflow;

import domain.MyAggregate;
import domain.MyAggregateKey;
import domain.MyFactory;
import domain.MyRepository;
import org.junit.Test;
import poussecafe.storable.StorableDefinition;
import poussecafe.test.MetaApplicationTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/*
 * MyWorkflowTest features the way of testing a work flow end-to-end. This means that a Pousse-Café context is actually
 * instantiated with data being stored in-memory (a default storage implementation is provided by the framework, it does
 * not need any additional code to be written). Commands and Domain Events are processed asynchronously and routed to
 * Workflow instances exactly as they will be in a production environment.
 */
public class MyProcessTest extends MetaApplicationTest {

    private MyAggregateKey key;

    private int x;

    /*
     * The context will be configured with provided actors (Domain Components and Work Flows).
     */
    @Override
    protected void registerComponents() {
        // First, let's register Domain components
        context().environment().defineStorable(new StorableDefinition.Builder()
                .withStorableClass(MyAggregate.class)
                .withFactoryClass(MyFactory.class)
                .withRepositoryClass(MyRepository.class)
                .build());

        // Second, let's register a work flow
        context().environment().defineProcess(MyProcess.class);
    }

    /*
     * Below test case verifies that the processing of a MyCommand instance actually leads to the update of targeted
     * Aggregate.
     */
    @Test
    public void myCommandUpdatesAggregate() {
        givenAvailableAggregate(); // Let's create an aggregate to execute a command against
        whenProcessingCommand(); // Now, let's to the actual execution of the command
        thenAggregateUpdated(); // Finally, let's check that the aggregate was properly updated
    }

    private void givenAvailableAggregate() {
        key = new MyAggregateKey("key");
        context().getProcess(MyProcess.class).handle(new CreateAggregate(key));
    }

    private void whenProcessingCommand() {
        x = 10;
        context().getProcess(MyProcess.class).handle(new MyCommand(key, x));
    }

    private void thenAggregateUpdated() {
        MyAggregate aggregate = find(MyAggregate.class, key);
        assertThat(aggregate.getX(), equalTo(x));
    }
}
