package poussecafe.doc.model.entitydoc;

import java.util.List;
import poussecafe.domain.AggregateRepository;

import static org.apache.commons.lang3.StringUtils.wrap;

public class EntityDocRepository
extends AggregateRepository<EntityDoc, EntityDocId, EntityDoc.Attributes> {

    public List<EntityDoc> findAll() {
        return wrap(dataAccess().findAll());
    }

    @Override
    public EntityDocDataAccess<EntityDoc.Attributes> dataAccess() {
        return (EntityDocDataAccess<EntityDoc.Attributes>) super.dataAccess();
    }
}
