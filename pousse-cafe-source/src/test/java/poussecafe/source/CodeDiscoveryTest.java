package poussecafe.source;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CodeDiscoveryTest {

    @Test
    public void findAggregates() throws IOException {
        givenScanner();
        whenIncludingTree(testModelDirectory);
        thenAggregateRootsFound();
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
}
