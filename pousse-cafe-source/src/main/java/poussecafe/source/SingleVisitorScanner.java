package poussecafe.source;

import java.io.IOException;
import java.nio.file.Path;
import poussecafe.source.analysis.ClassLoaderClassResolver;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.analysis.ResolvedCompilationUnitVisitor;
import poussecafe.source.analysis.TypeResolvingCompilationUnitVisitor;

public abstract class SingleVisitorScanner implements SourceConsumer {

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

    @Override
    public boolean providedPousseCafeResource(String sourceId) {
        return scanner.providedPousseCafeResource(sourceId);
    }

    @Override
    public boolean includedSource(String sourceId) {
        return scanner.includedSource(sourceId);
    }

    protected SingleVisitorScanner() {
        this(new ClassLoaderClassResolver());
    }

    protected SingleVisitorScanner(ClassResolver classResolver) {
        scanner = new SourceScanner(new TypeResolvingCompilationUnitVisitor.Builder()
                .withClassResolver(classResolver)
                .withVisitor(visitor())
                .build());
    }

    protected abstract ResolvedCompilationUnitVisitor visitor();
}
