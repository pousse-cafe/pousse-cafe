package poussecafe.source.generation;

import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.model.Aggregate;

public abstract class StorageAdaptersCodeGenerator extends AbstractCodeGenerator {

    public void generate(Aggregate aggregate) {
        updateDefaultAttributesImplementation(aggregate);
        addDataAccessImplementation(aggregate);
    }

    private void updateDefaultAttributesImplementation(Aggregate aggregate) {
        var typeName = NamingConventions.aggregateAttributesImplementationTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        updateDefaultAttributesImplementation(aggregate, compilationUnitEditor);
    }

    protected abstract void updateDefaultAttributesImplementation(Aggregate aggregate, CompilationUnitEditor compilationUnitEditor);

    private void addDataAccessImplementation(Aggregate aggregate) {
        var typeName = NamingConventions.aggregateDataAccessImplementationTypeName(aggregate, storageName());
        var compilationUnitEditor = compilationUnitEditor(typeName);
        if(compilationUnitEditor.isNew()) {
            addDataAccessImplementation(aggregate, compilationUnitEditor);
        }
    }

    protected abstract void addDataAccessImplementation(Aggregate aggregate, CompilationUnitEditor compilationUnitEditor);

    protected abstract String storageName();
}
