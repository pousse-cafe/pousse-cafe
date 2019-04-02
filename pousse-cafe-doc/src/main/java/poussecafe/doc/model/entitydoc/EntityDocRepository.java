package poussecafe.doc.model.entitydoc;

import java.util.List;
import poussecafe.domain.Repository;

public class EntityDocRepository extends Repository<EntityDoc, EntityDocId, EntityDoc.Attributes> {

    public List<EntityDoc> findAll() {
        return wrap(dataAccess().findAll());
    }

    @Override
    public EntityDocDataAccess<EntityDoc.Attributes> dataAccess() {
        return (EntityDocDataAccess<EntityDoc.Attributes>) super.dataAccess();
    }
}
