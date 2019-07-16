package poussecafe.doc.model.boundedcontextdoc;

import java.util.List;
import java.util.Optional;
import poussecafe.domain.Repository;

public class BoundedContextDocRepository extends Repository<BoundedContextDoc, BoundedContextDocId, BoundedContextDoc.Attributes> {

    public List<BoundedContextDoc> findAll() {
        return wrap(dataAccess().findAll());
    }

    @Override
    public BoundedContextDocDataAccess<BoundedContextDoc.Attributes> dataAccess() {
        return (BoundedContextDocDataAccess<BoundedContextDoc.Attributes>) super.dataAccess();
    }

    public Optional<BoundedContextDoc> findByPackageNamePrefixing(String name) {
        return wrapNullable(dataAccess().findByPackageNamePrefixing(name));
    }
}
