package poussecafe.doc.model.factorydoc;

import java.util.List;
import poussecafe.doc.model.moduledoc.ModuleDocId;
import poussecafe.domain.Repository;

public class FactoryDocRepository extends Repository<FactoryDoc, FactoryDocId, FactoryDoc.Attributes> {

    public List<FactoryDoc> findByModuleId(ModuleDocId id) {
        return wrap(dataAccess().findByModuleId(id));
    }

    @Override
    public FactoryDocDataAccess<FactoryDoc.Attributes> dataAccess() {
        return (FactoryDocDataAccess<FactoryDoc.Attributes>) super.dataAccess();
    }

    public List<FactoryDoc> findAll() {
        return wrap(dataAccess().findAll());
    }
}
