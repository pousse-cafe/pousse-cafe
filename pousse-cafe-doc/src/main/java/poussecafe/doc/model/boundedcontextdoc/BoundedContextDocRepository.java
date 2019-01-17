package poussecafe.doc.model.boundedcontextdoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc.Data;
import poussecafe.domain.Repository;

public class BoundedContextDocRepository extends Repository<BoundedContextDoc, BoundedContextDocKey, BoundedContextDoc.Data> {

    public List<BoundedContextDoc> findAll() {
        return wrap(dataAccess().findAll());
    }

    @Override
    public BoundedContextDocDataAccess<BoundedContextDoc.Data> dataAccess() {
        return (BoundedContextDocDataAccess<Data>) super.dataAccess();
    }

    public BoundedContextDoc findByPackageNamePrefixing(String name) {
        return wrap(dataAccess().findByPackageNamePrefixing(name));
    }
}
