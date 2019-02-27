package process;

import java.util.List;
import org.junit.Test;
import poussecafe.environment.BoundedContext;
import poussecafe.myboundedcontext.MyBoundedContext;
import poussecafe.myboundedcontext.domain.MyAggregate;
import poussecafe.myboundedcontext.domain.MyAggregateKey;
import poussecafe.myboundedcontext.process.DoSomeActionParameters;
import poussecafe.myboundedcontext.process.MyProcess;
import poussecafe.test.PousseCafeTest;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/*
 * This class features the way of testing a domain process end-to-end.
 */
public class MyProcessTest extends PousseCafeTest {

    /*
     * The context is configured with provided bounded contexts.
     */
    @Override
    protected List<BoundedContext> boundedContexts() {
        return asList(MyBoundedContext.configure().defineAndImplementDefault().build());
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
        parameters.key = new MyAggregateKey("key");
        myProcess.createMyAggregate(parameters.key);
    }

    private DoSomeActionParameters parameters = new DoSomeActionParameters();

    private MyProcess myProcess;

    private void whenProcessingCommand() {
        parameters.x = 10;
        myProcess.doSomeAction(parameters);
    }

    private void thenAggregateUpdated() {
        MyAggregate aggregate = find(MyAggregate.class, parameters.key);
        assertThat(aggregate.attributes().x().value(), equalTo(parameters.x));
    }
}
