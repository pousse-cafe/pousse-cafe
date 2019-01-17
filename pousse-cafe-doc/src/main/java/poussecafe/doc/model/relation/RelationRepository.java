package poussecafe.doc.model.relation;

import java.util.List;
import poussecafe.doc.model.relation.Relation.Data;
import poussecafe.domain.Repository;

public class RelationRepository extends Repository<Relation, RelationKey, Relation.Data> {

    public List<Relation> findWithFromClassName(String className) {
        return newEntitiesWithData(dataAccess().findWithFromClass(className));
    }

    @Override
    public RelationDataAccess<Relation.Data> dataAccess() {
        return (RelationDataAccess<Data>) super.dataAccess();
    }
}
