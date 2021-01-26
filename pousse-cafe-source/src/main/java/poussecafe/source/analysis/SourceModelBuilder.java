package poussecafe.source.analysis;

import poussecafe.source.SingleVisitorScanner;
import poussecafe.source.model.Model;

public class SourceModelBuilder extends SingleVisitorScanner {

    public Model build() {
        return visitor.buildModel();
    }

    @Override
    protected ResolvedCompilationUnitVisitor visitor() {
        visitor = new SourceModelBuilderVisitor();
        return visitor;
    }

    private SourceModelBuilderVisitor visitor;

    public SourceModelBuilder() {
        super();
    }

    public SourceModelBuilder(ClassResolver classResolver) {
        super(classResolver);
    }
}
