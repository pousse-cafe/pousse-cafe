package poussecafe.source;

import java.io.IOException;
import java.nio.file.Path;
import poussecafe.source.analysis.ClassLoaderClassResolver;
import poussecafe.source.model.Model;

public abstract class DiscoveryTest {

    protected void givenScanner() {
        modelVisitor = new ModelBuildingProjectVisitor(new ClassLoaderClassResolver());
        scanner = new SourceScanner(modelVisitor);
    }

    private ModelBuildingProjectVisitor modelVisitor;

    private SourceScanner scanner;

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
