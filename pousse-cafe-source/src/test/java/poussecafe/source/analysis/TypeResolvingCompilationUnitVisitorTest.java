package poussecafe.source.analysis;

import java.nio.file.Path;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;
import poussecafe.source.PathSource;
import poussecafe.source.SourceFile;

import static org.junit.Assert.assertTrue;

public class TypeResolvingCompilationUnitVisitorTest implements ResolvedCompilationUnitVisitor {

    @Test
    public void typeResolutionWithNestedTypes() {
        var visitor = new TypeResolvingCompilationUnitVisitor.Builder()
                .withClassResolver(new ClassLoaderClassResolver())
                .withVisitor(this)
                .build();
        ASTParser parser = ASTParser.newParser(AST.JLS14);
        PathSource source = new PathSource(Path.of("", "src", "test", "java", "poussecafe", "source", "testmodel", "model", "aggregate1", "Aggregate1.java"));
        source.configure(parser);
        CompilationUnit unit = (CompilationUnit) parser.createAST(null);
        visitor.visit(new SourceFile.Builder()
                .source(source)
                .tree(unit)
                .build());
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
