package poussecafe.doc.model.boundedcontextdoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc.Data;
import poussecafe.domain.Repository;

public class BoundedContextDocRepository extends Repository<BoundedContextDoc, String, BoundedContextDoc.Data> {

    public List<BoundedContextDoc> findAll() {
        return newStorablesWithData(dataAccess().findAll());
    }

    private BoundedContextDocDataAccess<BoundedContextDoc.Data> dataAccess() {
        return (BoundedContextDocDataAccess<Data>) dataAccess;
    }
}
