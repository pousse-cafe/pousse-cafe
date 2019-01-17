package poussecafe.doc.model.entitydoc;

import java.util.List;
import poussecafe.domain.Repository;

public class EntityDocRepository extends Repository<EntityDoc, EntityDocKey, EntityDoc.Data> {

    public List<EntityDoc> findAll() {
        return newEntitiesWithData(dataAccess().findAll());
    }

    @Override
    public EntityDocDataAccess<EntityDoc.Data> dataAccess() {
        return (EntityDocDataAccess<EntityDoc.Data>) super.dataAccess();
    }
}
