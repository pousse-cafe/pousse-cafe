package poussecafe.source;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.Test;
import poussecafe.source.testmodel.events.Event1;
import poussecafe.source.testmodel.events.Event2;

import static org.junit.Assert.assertTrue;

public class CodeDiscoveryTest {

    @Test
    public void findAggregates() throws IOException {
        givenScanner();
        whenIncludingTree(testModelDirectory);
        thenAggregateRootsFound();
        thenListenersFound();
    }

    private void givenScanner() {
        scanner = new Scanner.Builder().build();
    }

    private Scanner scanner;

    private void whenIncludingTree(Path sourceFilePath) throws IOException {
        scanner.includeTree(sourceFilePath);
    }

    private Path testModelDirectory = Path.of("", "src", "test", "java", "poussecafe", "source", "testmodel");

    private void thenAggregateRootsFound() {
        assertTrue(scanner.registry().aggregateRoot("Aggregate1").isPresent());
        assertTrue(scanner.registry().aggregateRoot("Aggregate2").isPresent());
    }

    private void thenListenersFound() {
        assertTrue(scanner.registry().aggregateRoot("Aggregate1").orElseThrow().messageListener("process1Listener1", Event1.class).isPresent());
        assertTrue(scanner.registry().aggregateRoot("Aggregate2").orElseThrow().messageListener("process1Listener2", Event2.class).isPresent());
    }
}
