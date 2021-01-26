package poussecafe.source.validation;

import poussecafe.source.SingleVisitorScanner;
import poussecafe.source.analysis.ClassResolver;
import poussecafe.source.analysis.ResolvedCompilationUnitVisitor;
import poussecafe.source.validation.model.ValidationModel;

public class ValidationModelBuilder extends SingleVisitorScanner {

    public ValidationModel build() {
        return visitor.buildModel();
    }

    @Override
    protected ResolvedCompilationUnitVisitor visitor() {
        visitor = new ValidationModelBuilderVisitor();
        return visitor;
    }

    private ValidationModelBuilderVisitor visitor;

    public ValidationModelBuilder() {
        super();
    }

    public ValidationModelBuilder(ClassResolver classResolver) {
        super(classResolver);
    }
}
