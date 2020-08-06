package poussecafe.processing;

import java.time.Duration;
import org.junit.Test;
import poussecafe.testmodule.SimpleAggregateId;
import poussecafe.testmodule.TestDomainEvent3;
import poussecafe.testmodule.TestModuleTestCase;

import static org.junit.Assert.assertTrue;

public class UpdateOrCreateTest extends TestModuleTestCase {

    @Test
    public void throwingRunnerDoesNotTriggerInfiniteLoop() {
        TestDomainEvent3 event = runtime().newDomainEvent(TestDomainEvent3.class);
        event.identifier().value(new SimpleAggregateId("id3"));
        runtime().issue(event);
        waitUntilEndOfMessageProcessingOfElapsed(Duration.ofSeconds(3600));
        assertTrue(true);
    }
}
