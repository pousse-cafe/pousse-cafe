package poussecafe.source;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.analysis.CompilationUnitResolver;
import poussecafe.source.model.Model;
import poussecafe.source.model.ModelBuilder;

import static java.util.Objects.requireNonNull;

class ModelBuildingProjectVisitor implements SourceFileVisitor {

    @Override
    public void visitFile(SourceFile sourceFile) {
        var compilationUnit = sourceFile.tree();
        var compilationUnitVisitor = compilationUnitVisitor(compilationUnit);
        compilationUnit.accept(compilationUnitVisitor);
    }

    private ASTVisitor compilationUnitVisitor(CompilationUnit compilationUnit) {
        return new ModelBuildingCompilationUnitVisitor.Builder()
                .compilationUnitResolver(new CompilationUnitResolver.Builder()
                        .compilationUnit(compilationUnit)
                        .classResolver(classResolver)
                        .build())
                .model(model)
                .build();
    }

    private ClassResolver classResolver;

    private ModelBuilder model = new ModelBuilder();

    public Model buildModel() {
        return model.build();
    }

    ModelBuildingProjectVisitor(ClassResolver classResolver) {
        requireNonNull(classResolver);
        this.classResolver = classResolver;
    }
}
