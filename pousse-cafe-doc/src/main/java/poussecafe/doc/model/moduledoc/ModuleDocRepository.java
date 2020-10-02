package poussecafe.doc.model.moduledoc;

import java.util.List;
import java.util.Optional;
import poussecafe.domain.AggregateRepository;

public class ModuleDocRepository
extends AggregateRepository<ModuleDoc, ModuleDocId, ModuleDoc.Attributes> {

    public List<ModuleDoc> findAll() {
        return wrap(dataAccess().findAll());
    }

    @Override
    public ModuleDocDataAccess<ModuleDoc.Attributes> dataAccess() {
        return (ModuleDocDataAccess<ModuleDoc.Attributes>) super.dataAccess();
    }

    public Optional<ModuleDoc> findByPackageNamePrefixing(String name) {
        return wrapNullable(dataAccess().findByPackageNamePrefixing(name));
    }
}
