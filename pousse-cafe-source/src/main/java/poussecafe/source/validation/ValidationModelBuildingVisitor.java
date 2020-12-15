package poussecafe.source.validation;

import org.eclipse.jdt.core.dom.ASTVisitor;
import poussecafe.source.SourceFile;
import poussecafe.source.SourceFileVisitor;

public class ValidationModelBuildingVisitor implements SourceFileVisitor {

    @Override
    public void visitFile(SourceFile sourceFile) {
        var compilationUnitVisitor = compilationUnitVisitor(sourceFile);
        sourceFile.tree().accept(compilationUnitVisitor);
    }

    private ASTVisitor compilationUnitVisitor(SourceFile sourceFile) {
        return new ValidationCompilationUnitVisitor.Builder()
                .sourceFile(sourceFile)
                .model(model)
                .build();
    }

    private ValidationModel model = new ValidationModel();

    public ValidationModel buildModel() {
        return model;
    }
}
