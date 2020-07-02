package poussecafe.source;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.Test;
import poussecafe.source.model.AggregateRootSource;
import poussecafe.source.model.MessageListenerSource;

import static org.junit.Assert.assertTrue;

public class AggregateDiscoveryTest {

    @Test
    public void findAggregates() throws IOException {
        givenScanner();
        whenIncludingTree(testModelDirectory);
        thenAggregateRootsFound();
        thenAggregateListenersFound();
    }

    private void givenScanner() {
        scanner = new Scanner.Builder().build();
    }

    private Scanner scanner;

    private void whenIncludingTree(Path sourceTreePath) throws IOException {
        scanner.includeTree(sourceTreePath);
    }

    private Path testModelDirectory = Path.of("", "src", "test", "java", "poussecafe", "source", "testmodel");

    private void thenAggregateRootsFound() {
        assertTrue(aggregateRoot("Aggregate1").isPresent());
        assertTrue(aggregateRoot("Aggregate2").isPresent());
    }

    private Optional<AggregateRootSource> aggregateRoot(String name) {
        return scanner.model().aggregateRoot(name);
    }

    private void thenAggregateListenersFound() {
        Optional<MessageListenerSource> listener1 = aggregateMessageListener("Aggregate1", "process1Listener1", "Event1");
        assertTrue(listener1.isPresent());
        assertTrue(listener1.orElseThrow().processNames().contains("Process1"));

        Optional<MessageListenerSource> listener2 = aggregateMessageListener("Aggregate2", "process1Listener2", "Event2");
        assertTrue(listener2.isPresent());
        assertTrue(listener2.orElseThrow().processNames().contains("Process1"));
    }

    private Optional<MessageListenerSource> aggregateMessageListener(String aggregateName, String listenerName, String messageName) {
        return aggregateRoot(aggregateName).orElseThrow().messageListener(listenerName, messageName);
    }
}
