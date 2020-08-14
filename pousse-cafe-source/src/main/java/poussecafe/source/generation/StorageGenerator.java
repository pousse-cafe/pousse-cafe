package poussecafe.source.generation;

import poussecafe.source.model.Aggregate;

public abstract class StorageGenerator extends AbstractCodeGenerator {

    public void generate(Aggregate aggregate) {
        updateDefaultAttributesImplementation(aggregate);
        addDataAccessImplementation(aggregate);
    }

    private void updateDefaultAttributesImplementation(Aggregate aggregate) {
        var typeName = AggregateCodeGenerationConventions.aggregateAttributesImplementationTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        updateDefaultAttributesImplementation(aggregate, compilationUnitEditor);
    }

    protected abstract void updateDefaultAttributesImplementation(Aggregate aggregate, ComilationUnitEditor compilationUnitEditor);

    private void addDataAccessImplementation(Aggregate aggregate) {
        var typeName = AggregateCodeGenerationConventions.aggregateDataAccessImplementationTypeName(aggregate, storageName());
        var compilationUnitEditor = compilationUnitEditor(typeName);
        addDataAccessImplementation(aggregate, compilationUnitEditor);
    }

    protected abstract void addDataAccessImplementation(Aggregate aggregate, ComilationUnitEditor compilationUnitEditor);

    protected abstract String storageName();
}
