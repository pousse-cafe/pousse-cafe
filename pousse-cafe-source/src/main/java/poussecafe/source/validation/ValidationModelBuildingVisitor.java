package poussecafe.source.validation;

import org.eclipse.jdt.core.dom.ASTVisitor;
import poussecafe.source.SourceFile;
import poussecafe.source.SourceFileVisitor;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.validation.model.ValidationModel;

class ValidationModelBuildingVisitor implements SourceFileVisitor {

    @Override
    public void visitFile(SourceFile sourceFile) {
        var compilationUnitVisitor = compilationUnitVisitor(sourceFile);
        sourceFile.tree().accept(compilationUnitVisitor);
    }

    private ASTVisitor compilationUnitVisitor(SourceFile sourceFile) {
        return new ValidationCompilationUnitVisitor.Builder()
                .sourceFile(sourceFile)
                .model(model)
                .classResolver(classResolver)
                .build();
    }

    private ValidationModel model = new ValidationModel();

    private ClassResolver classResolver;

    public ValidationModel buildModel() {
        return model;
    }

    ValidationModelBuildingVisitor(ClassResolver classResolver) {
        this.classResolver = classResolver;
    }
}
