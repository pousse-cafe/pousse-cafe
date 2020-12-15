package poussecafe.source;

import java.io.IOException;
import java.nio.file.Path;
import poussecafe.source.model.Model;

public class SourceModelBuilder implements SourceConsumer {

    public Model build() {
        return visitor.buildModel();
    }

    private ModelBuildingProjectVisitor visitor;

    @Override
    public void includeFile(Path sourceFilePath) throws IOException {
        scanner.includeFile(sourceFilePath);
    }

    private SourceScanner scanner;

    @Override
    public void includeTree(Path sourceDirectory) throws IOException {
        scanner.includeTree(sourceDirectory);
    }

    public SourceModelBuilder() {
        visitor = new ModelBuildingProjectVisitor();
        scanner = new SourceScanner(visitor);
    }
}
