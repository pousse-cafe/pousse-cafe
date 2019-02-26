package process;

import java.util.List;
import org.junit.Test;
import poussecafe.environment.BoundedContext;
import poussecafe.simple.MyBoundedContextConfiguration;
import poussecafe.simple.domain.MyAggregate;
import poussecafe.simple.domain.MyAggregateKey;
import poussecafe.simple.process.CreateAggregate;
import poussecafe.simple.process.MyCommand;
import poussecafe.simple.process.MyProcess;
import poussecafe.test.PousseCafeTest;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/*
 * This class features the way of testing a domain process end-to-end.
 */
public class MyProcessTest extends PousseCafeTest {

    private MyAggregateKey key;

    private int x;

    /*
     * The context is configured with provided bounded contexts.
     */
    @Override
    protected List<BoundedContext> boundedContexts() {
        return asList(MyBoundedContextConfiguration.configure().defineAndImplementDefault().build());
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
        myProcess.handle(new CreateAggregate(key));
    }

    private MyProcess myProcess;

    private void whenProcessingCommand() {
        x = 10;
        myProcess.handle(new MyCommand(key, x));
    }

    private void thenAggregateUpdated() {
        MyAggregate aggregate = find(MyAggregate.class, key);
        assertThat(aggregate.attributes().x().value(), equalTo(x));
    }
}
