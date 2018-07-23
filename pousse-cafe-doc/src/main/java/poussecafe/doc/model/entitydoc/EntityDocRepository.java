package poussecafe.doc.model.entitydoc;

import java.util.List;
import poussecafe.domain.Repository;

public class EntityDocRepository extends Repository<EntityDoc, EntityDocKey, EntityDoc.Data> {

    public List<EntityDoc> findAll() {
        return newStorablesWithData(dataAccess().findAll());
    }

    private EntityDocDataAccess<EntityDoc.Data> dataAccess() {
        return (EntityDocDataAccess<EntityDoc.Data>) dataAccess;
    }
}
