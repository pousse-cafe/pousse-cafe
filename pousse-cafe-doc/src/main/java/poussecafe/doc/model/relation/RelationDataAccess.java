package poussecafe.doc.model.relation;

import java.util.List;
import poussecafe.domain.EntityDataAccess;

public interface RelationDataAccess<D extends Relation.Attributes> extends EntityDataAccess<RelationKey, D> {

    List<D> findWithFromClass(String className);

}
