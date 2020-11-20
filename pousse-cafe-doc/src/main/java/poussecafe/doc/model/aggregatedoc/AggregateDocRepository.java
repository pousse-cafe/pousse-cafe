package poussecafe.doc.model.aggregatedoc;

import java.util.List;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.AggregateRepository;

public class AggregateDocRepository
extends AggregateRepository<AggregateDocId, AggregateDoc, AggregateDoc.Attributes> {

    public List<AggregateDoc> findByModule(ModuleDocId id) {
        return wrap(dataAccess().findByModuleId(id));
    }

    @Override
    public AggregateDocDataAccess<AggregateDoc.Attributes> dataAccess() {
        return (AggregateDocDataAccess<AggregateDoc.Attributes>) super.dataAccess();
    }

    public List<AggregateDoc> findAll() {
        return wrap(dataAccess().findAll());
    }

    public List<AggregateDoc> findByIdClassName(String qualifiedName) {
        return wrap(dataAccess().findByIdClassName(qualifiedName));
    }
}
