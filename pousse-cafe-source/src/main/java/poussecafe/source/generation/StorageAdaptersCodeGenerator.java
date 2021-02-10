package poussecafe.source.generation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.model.Aggregate;

public abstract class StorageAdaptersCodeGenerator extends AbstractCodeGenerator {

    public void generate(Aggregate aggregate) {
        updateDefaultAttributesImplementation(aggregate);
        addDataAccessImplementation(aggregate);
    }

    private void updateDefaultAttributesImplementation(Aggregate aggregate) {
        var typeName = NamingConventions.aggregateAttributesImplementationTypeName(aggregate.aggregatePackage());
        var compilationUnitEditor = compilationUnitEditor(typeName);
        if(!compilationUnitEditor.isNew()) {
            updateDefaultAttributesImplementation(aggregate, compilationUnitEditor);
        } else {
            logger.warn("Could not update default attributes implementation {}, check naming convention", typeName);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract void updateDefaultAttributesImplementation(Aggregate aggregate, CompilationUnitEditor compilationUnitEditor);

    private void addDataAccessImplementation(Aggregate aggregate) {
        var typeName = NamingConventions.aggregateDataAccessImplementationTypeName(aggregate.aggregatePackage(), storageName());
        var compilationUnitEditor = compilationUnitEditor(typeName);
        if(compilationUnitEditor.isNew()) {
            addDataAccessImplementation(aggregate, compilationUnitEditor);
        } else {
            logger.debug("Data access implementation {} already exists, skipping creation", typeName);
        }
    }

    protected abstract void addDataAccessImplementation(Aggregate aggregate, CompilationUnitEditor compilationUnitEditor);

    protected abstract String storageName();
}
