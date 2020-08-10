package poussecafe.discovery;

import java.time.Duration;
import org.junit.Test;
import poussecafe.testmodule2.CreateSimpleAggregate;
import poussecafe.testmodule2.SimpleAggregateId;
import poussecafe.testmodule2.TestModule2TestCase;

import static org.junit.Assert.assertTrue;

public class AggregateContainerTest extends TestModule2TestCase {

    @Test
    public void runtimeWorks() {
        CreateSimpleAggregate command = runtime().newCommand(CreateSimpleAggregate.class);
        command.identifier().value(new SimpleAggregateId("id3"));
        runtime().submitCommand(command);
        waitUntilEndOfMessageProcessingOfElapsed(Duration.ofSeconds(3600));
        assertTrue(true);
    }
}
