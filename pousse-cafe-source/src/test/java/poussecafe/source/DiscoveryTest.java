package poussecafe.source;

import java.io.IOException;
import java.nio.file.Path;
import poussecafe.source.model.Model;

public abstract class DiscoveryTest {

    protected void givenScanner() {
        scanner = new Scanner();
    }

    private Scanner scanner;

    protected void whenIncludingTree(Path sourceTreePath) throws IOException {
        scanner.includeTree(sourceTreePath);
    }

    protected void whenIncludingTestModelTree() throws IOException {
        whenIncludingTree(testModelDirectory);
    }

    public static final Path testModelDirectory = Path.of("", "src", "test", "java", "poussecafe", "source", "testmodel");

    protected Model model() {
        return scanner.model();
    }

    protected String basePackage() {
        return "poussecafe.source.testmodel";
    }
}
