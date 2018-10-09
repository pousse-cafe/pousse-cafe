package poussecafe.doc.model.boundedcontextdoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc.Data;
import poussecafe.domain.Repository;

public class BoundedContextDocRepository extends Repository<BoundedContextDoc, BoundedContextDocKey, BoundedContextDoc.Data> {

    public List<BoundedContextDoc> findAll() {
        return newEntitiesWithData(dataAccess().findAll());
    }

    private BoundedContextDocDataAccess<BoundedContextDoc.Data> dataAccess() {
        return (BoundedContextDocDataAccess<Data>) dataAccess;
    }

    public BoundedContextDoc findByPackageNamePrefixing(String name) {
        return newEntityWithData(dataAccess().findByPackageNamePrefixing(name));
    }
}
