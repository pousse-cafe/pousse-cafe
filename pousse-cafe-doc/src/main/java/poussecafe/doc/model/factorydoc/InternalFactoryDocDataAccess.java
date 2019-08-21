package poussecafe.doc.model.factorydoc;

import java.util.List;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.stream.Collectors.toList;

@DataAccessImplementation(
    aggregateRoot = FactoryDoc.class,
    dataImplementation = FactoryDocData.class,
    storageName = InternalStorage.NAME
)
public class InternalFactoryDocDataAccess extends InternalDataAccess<FactoryDocId, FactoryDocData> implements FactoryDocDataAccess<FactoryDocData> {

    @Override
    public List<FactoryDocData> findByModuleId(ModuleDocId id) {
        return findAll().stream().filter(data -> data.moduleComponentDoc().value().moduleDocId().equals(id)).collect(toList());
    }

    @Override
    public FactoryDocData findByModuleIdAndName(ModuleDocId moduleDocId,
            String aggregateName) {
        return findAll().stream()
                .filter(data -> data.moduleComponentDoc().value().moduleDocId().equals(moduleDocId))
                .filter(data -> data.moduleComponentDoc().value().componentDoc().name().equals(aggregateName))
                .findFirst().orElse(null);
    }
}
