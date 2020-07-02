package poussecafe.source;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CodeDiscoveryTest {

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
        assertTrue(aggregateRoot("Aggregate1").orElseThrow().messageListener("process1Listener1", "Event1").isPresent());
        assertTrue(aggregateRoot("Aggregate2").orElseThrow().messageListener("process1Listener2", "Event2").isPresent());
    }
}
