package poussecafe.doc.model.processstepdoc;

import poussecafe.discovery.DataAccessImplementation;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.exception.PousseCafeException;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

@DataAccessImplementation(
    aggregateRoot = ProcessStepDoc.class,
    dataImplementation = ProcessStepDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalProcessStepDocDataAccess extends InternalDataAccess<ProcessStepDocKey, ProcessStepDocData> implements ProcessStepDataAccess<ProcessStepDocData> {

    @Override
    public ProcessStepDocData getByStepMethodSignature(BoundedContextDocKey boundedContextDocKey,
            StepMethodSignature stepMethodSignature) {
        return findAll().stream()
                .filter(data -> data.boundedContextComponentDoc().value().boundedContextDocKey().equals(boundedContextDocKey))
                .filter(data -> data.stepMethodSignature().valueEquals(stepMethodSignature))
                .findFirst().orElseThrow(() -> new PousseCafeException("No process step doc for signature " + stepMethodSignature));
    }
}
