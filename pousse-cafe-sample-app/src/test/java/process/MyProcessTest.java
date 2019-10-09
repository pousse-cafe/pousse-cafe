package process;

import org.junit.Test;
import poussecafe.mymodule.domain.myaggregate.MyAggregate;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;
import poussecafe.mymodule.process.MyProcess;
import poussecafe.mymodule.process.MyProcess.DoSomethingParameters;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/*
 * Verifies that MyProcess domain process behaves as expected.
 */
public class MyProcessTest extends MyModuleTest {

    /*
     * Test the creation and update of a MyAggregate instance via MyProcess.
     */
    @Test
    public void myCommandUpdatesAggregate() {
        givenAvailableAggregate(); // Let's create an aggregate to execute a command against
        whenProcessingCommand(); // Now, let's to the actual execution of the command
        thenAggregateUpdated(); // Finally, let's check that the aggregate was properly updated
    }

    private void givenAvailableAggregate() {
        parameters.id = new MyAggregateId("id");
        myProcess.createMyAggregate(parameters.id);
    }

    private MyProcess.DoSomethingParameters parameters = new DoSomethingParameters();

    private MyProcess myProcess;

    private void whenProcessingCommand() {
        parameters.x = 10;
        myProcess.doSomeAction(parameters);
    }

    private void thenAggregateUpdated() {
        MyAggregate aggregate = getOptional(MyAggregate.class, parameters.id).orElseThrow();
        assertThat(aggregate.attributes().x().value(), equalTo(parameters.x));
    }
}
