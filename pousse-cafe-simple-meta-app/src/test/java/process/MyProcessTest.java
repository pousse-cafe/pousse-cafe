package process;

import configuration.MyMetaApplicationBundle;
import domain.MyAggregate;
import domain.MyAggregateKey;
import org.junit.Test;
import poussecafe.context.MetaApplicationBundle;
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
    protected MetaApplicationBundle testBundle() {
        return new MyMetaApplicationBundle();
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
        context().getDomainProcess(MyProcess.class).handle(new CreateAggregate(key));
    }

    private void whenProcessingCommand() {
        x = 10;
        context().getDomainProcess(MyProcess.class).handle(new MyCommand(key, x));
    }

    private void thenAggregateUpdated() {
        MyAggregate aggregate = find(MyAggregate.class, key);
        assertThat(aggregate.getX(), equalTo(x));
    }
}
