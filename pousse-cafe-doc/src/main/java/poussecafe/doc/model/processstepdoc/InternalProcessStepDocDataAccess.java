package poussecafe.doc.model.processstepdoc;

import java.util.List;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.stream.Collectors.toList;

@DataAccessImplementation(
    aggregateRoot = ProcessStepDoc.class,
    dataImplementation = ProcessStepDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalProcessStepDocDataAccess extends InternalDataAccess<ProcessStepDocId, ProcessStepDocData> implements ProcessStepDataAccess<ProcessStepDocData> {

    @Override
    public List<ProcessStepDocData> findByDomainProcess(ModuleDocId moduleDocId,
            String processName) {
        return findAll().stream()
                .filter(data -> data.processName().value().isPresent())
                .filter(data -> data.moduleComponentDoc().value().moduleDocId().equals(moduleDocId))
                .filter(data -> data.processName().value().get().equals(processName))
                .collect(toList());
    }

    @Override
    public List<ProcessStepDocData> findConsuming(ModuleDocId moduleDocId,
            String eventName) {
        return findAll().stream()
                .filter(data -> data.processName().value().isPresent())
                .filter(data -> data.moduleComponentDoc().value().moduleDocId().equals(moduleDocId))
                .filter(data -> data.stepMethodSignature().value().isPresent())
                .filter(data -> data.stepMethodSignature().value().get().consumedEventName().isPresent())
                .filter(data -> data.stepMethodSignature().value().get().consumedEventName().get().equals(eventName))
                .collect(toList());
    }

    @Override
    public List<ProcessStepDocData> findProducing(ModuleDocId moduleDocId,
            String eventName) {
        return findAll().stream()
                .filter(data -> data.processName().value().isPresent())
                .filter(data -> data.moduleComponentDoc().value().moduleDocId().equals(moduleDocId))
                .filter(data -> data.producedEvents().contains(eventName))
                .collect(toList());
    }
}
