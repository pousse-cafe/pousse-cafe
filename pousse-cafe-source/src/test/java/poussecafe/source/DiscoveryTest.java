package poussecafe.source;

import java.io.IOException;
import java.nio.file.Path;
import poussecafe.source.analysis.ClassLoaderClassResolver;
import poussecafe.source.analysis.SourceModelBuilder;
import poussecafe.source.model.SourceModel;

public abstract class DiscoveryTest {

    protected void givenScanner() {
        modelBuilder = new SourceModelBuilder(new ClassLoaderClassResolver());
    }

    private SourceModelBuilder modelBuilder;

    protected void whenIncludingTree(Path sourceTreePath) throws IOException {
        modelBuilder.includeTree(sourceTreePath);
    }

    protected void whenIncludingTestModelTree() throws IOException {
        whenIncludingTree(testModelDirectory);
    }

    public static final Path testModelDirectory = Path.of("", "src", "test", "java", "poussecafe", "source", "testmodel");

    protected SourceModel model() {
        return modelBuilder.build();
    }

    protected String basePackage() {
        return "poussecafe.source.testmodel";
    }
}
