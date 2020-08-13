package poussecafe.source.generation;

import poussecafe.source.model.Aggregate;

public class InternalStorageCodeGenerator extends AbstractCodeGenerator {

    public void edit() {
        enhanceImplementation();
    }

    private void enhanceImplementation() {
        var typeName = AggregateCodeGenerationConventions.aggregateAttributesImplementationTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateFactoryEditor = new InternalAttributesImplementationEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateFactoryEditor.edit();
    }

    private Aggregate aggregate;
}
