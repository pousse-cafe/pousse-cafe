package poussecafe.source;

import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.model.Model;
import poussecafe.source.model.ModelBuilder;

import static java.util.Objects.requireNonNull;

class ModelBuildingProjectVisitor implements SourceFileVisitor {

    @Override
    public void visitFile(SourceFile sourceFile) {
        visitor = compilationUnitVisitor(sourceFile);
        sourceFile.tree().accept(visitor);
    }

    private ModelBuildingCompilationUnitVisitor visitor;

    private ModelBuildingCompilationUnitVisitor compilationUnitVisitor(SourceFile sourceFile) {
        return new ModelBuildingCompilationUnitVisitor.Builder()
                .sourceFile(sourceFile)
                .classResolver(classResolver)
                .modelBuilder(builder)
                .build();
    }

    private ClassResolver classResolver;

    private ModelBuilder builder = new ModelBuilder();

    public Model buildModel() {
        return builder.build();
    }

    ModelBuildingProjectVisitor(ClassResolver classResolver) {
        requireNonNull(classResolver);
        this.classResolver = classResolver;
    }
}
