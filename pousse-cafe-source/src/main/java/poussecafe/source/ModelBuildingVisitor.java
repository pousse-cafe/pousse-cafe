package poussecafe.source;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import poussecafe.source.model.Model;
import poussecafe.source.model.ModelBuilder;

public class ModelBuildingVisitor implements SourceFileVisitor {

    @Override
    public void visitFile(SourceFile sourceFile) {
        var compilationUnit = sourceFile.tree();
        var compilationUnitVisitor = compilationUnitVisitor(compilationUnit);
        compilationUnit.accept(compilationUnitVisitor);
    }

    private ASTVisitor compilationUnitVisitor(CompilationUnit compilationUnit) {
        return new CompilationUnitVisitor.Builder()
                .compilationUnit(compilationUnit)
                .model(model)
                .build();
    }

    private ModelBuilder model = new ModelBuilder();

    public Model buildModel() {
        return model.build();
    }
}
