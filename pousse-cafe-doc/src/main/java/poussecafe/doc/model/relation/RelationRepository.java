package poussecafe.doc.model.relation;

import java.util.List;
import poussecafe.domain.Repository;

public class RelationRepository extends Repository<Relation, RelationId, Relation.Attributes> {

    public List<Relation> findWithFromClassName(String className) {
        return wrap(dataAccess().findWithFromClass(className));
    }

    @Override
    public RelationDataAccess<Relation.Attributes> dataAccess() {
        return (RelationDataAccess<Relation.Attributes>) super.dataAccess();
    }
}
