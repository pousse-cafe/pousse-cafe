package poussecafe.source;

import java.io.IOException;
import java.util.Optional;
import org.junit.Test;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.MessageListener;

import static org.junit.Assert.assertTrue;

public class AggregateDiscoveryTest extends DiscoveryTest {

    @Test
    public void findAggregates() throws IOException {
        givenScanner();
        whenIncludingTestModelTree();
        thenAggregateRootsFound();
        thenAggregateListenersFound();
    }

    private void thenAggregateRootsFound() {
        assertTrue(aggregateRoot("Aggregate1").isPresent());
        assertTrue(aggregateRoot("Aggregate2").isPresent());
    }

    private Optional<Aggregate> aggregateRoot(String name) {
        return model().aggregateRoot(name);
    }

    private void thenAggregateListenersFound() {
        Optional<MessageListener> listener0 = aggregateMessageListener("Aggregate1", "process1Listener0", "Command1");
        assertTrue(listener0.isPresent());

        Optional<MessageListener> listener1 = aggregateMessageListener("Aggregate1", "process1Listener1", "Event1");
        assertTrue(listener1.isPresent());

        Optional<MessageListener> listener2 = aggregateMessageListener("Aggregate2", "process1Listener2", "Event2");
        assertTrue(listener2.isPresent());

        Optional<MessageListener> listener3 = aggregateMessageListener("Aggregate2", "process1Listener3", "Command2");
        assertTrue(listener3.isPresent());
    }

    private Optional<MessageListener> aggregateMessageListener(String aggregateName, String listenerName, String messageName) {
        return model().aggregateListeners(aggregateName).stream()
                .filter(listener -> listener.methodName().equals(listenerName))
                .filter(listener -> listener.messageName().equals(messageName))
                .findFirst();
    }
}
