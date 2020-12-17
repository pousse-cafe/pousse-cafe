package poussecafe.source;

import java.io.IOException;
import java.nio.file.Path;
import poussecafe.source.analysis.ClassLoaderClassResolver;
import poussecafe.source.analysis.ClassResolver;
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

    @Override
    public void includeSource(Source source) {
        scanner.includeSource(source);
    }

    public SourceModelBuilder() {
        this(new ClassLoaderClassResolver());
    }

    public SourceModelBuilder(ClassResolver classResolver) {
        visitor = new ModelBuildingProjectVisitor(classResolver);
        scanner = new SourceScanner(visitor);
    }
}
