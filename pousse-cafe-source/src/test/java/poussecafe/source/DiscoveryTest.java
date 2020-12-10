package poussecafe.source;

import java.io.IOException;
import java.nio.file.Path;
import poussecafe.source.model.Model;

public abstract class DiscoveryTest {

    protected void givenScanner() {
        modelVisitor = new ModelBuildingVisitor();
        scanner = new Scanner(modelVisitor);
    }

    private ModelBuildingVisitor modelVisitor;

    private Scanner scanner;

    protected void whenIncludingTree(Path sourceTreePath) throws IOException {
        scanner.includeTree(sourceTreePath);
    }

    protected void whenIncludingTestModelTree() throws IOException {
        whenIncludingTree(testModelDirectory);
    }

    public static final Path testModelDirectory = Path.of("", "src", "test", "java", "poussecafe", "source", "testmodel");

    protected Model model() {
        return modelVisitor.buildModel();
    }

    protected String basePackage() {
        return "poussecafe.source.testmodel";
    }
}
