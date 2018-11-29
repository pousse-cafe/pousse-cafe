package process;

import java.util.List;
import org.junit.Test;
import poussecafe.context.BoundedContext;
import poussecafe.simplemetaapp.MyBoundedContextDefinition;
import poussecafe.simplemetaapp.domain.MyAggregate;
import poussecafe.simplemetaapp.domain.MyAggregateKey;
import poussecafe.simplemetaapp.process.CreateAggregate;
import poussecafe.simplemetaapp.process.MyCommand;
import poussecafe.simplemetaapp.process.MyProcess;
import poussecafe.test.MetaApplicationTest;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/*
 * This class features the way of testing a domain process end-to-end.
 */
public class MyProcessTest extends MetaApplicationTest {

    private MyAggregateKey key;

    private int x;

    /*
     * The context is configured with provided bounded contexts.
     */
    @Override
    protected List<BoundedContext> testBundle() {
        return asList(new MyBoundedContextDefinition().withDefaultImplementation().build());
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
        assertThat(aggregate.x().let(this).get(), equalTo(x));
    }
}
