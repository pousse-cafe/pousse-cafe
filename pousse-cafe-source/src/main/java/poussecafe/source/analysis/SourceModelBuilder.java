package poussecafe.source.analysis;

import poussecafe.source.SingleVisitorScanner;
import poussecafe.source.model.SourceModel;

public class SourceModelBuilder extends SingleVisitorScanner {

    public SourceModel build() {
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
