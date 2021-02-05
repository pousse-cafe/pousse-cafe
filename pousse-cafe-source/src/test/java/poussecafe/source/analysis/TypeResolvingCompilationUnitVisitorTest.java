package poussecafe.source.analysis;

import java.nio.file.Path;
import org.junit.Test;
import poussecafe.source.PathSource;

import static org.junit.Assert.assertTrue;

public class TypeResolvingCompilationUnitVisitorTest implements ResolvedCompilationUnitVisitor {

    @Test
    public void typeResolutionWithNestedTypes() {
        var visitor = new TypeResolvingCompilationUnitVisitor.Builder()
                .withClassResolver(new ClassLoaderClassResolver())
                .withVisitor(this)
                .build();
        PathSource source = new PathSource(Path.of("", "src", "test", "java", "poussecafe", "source", "testmodel", "model", "aggregate1", "Aggregate1.java"));
        visitor.visit(source);
        assertTrue(foundRoot);
    }

    @Override
    public boolean visit(ResolvedTypeDeclaration type) {
        if(type.unresolvedName().qualifiedName().equals("poussecafe.source.testmodel.model.aggregate1.Aggregate1.Root")) {
            foundRoot = AggregateRootClass.isAggregateRoot(type);
        }
        return true;
    }

    private boolean foundRoot;
}
