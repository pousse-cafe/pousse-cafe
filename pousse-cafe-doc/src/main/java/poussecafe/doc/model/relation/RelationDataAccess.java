package poussecafe.doc.model.relation;

import java.util.List;
import poussecafe.storable.IdentifiedStorableDataAccess;

public interface RelationDataAccess<D extends Relation.Data> extends IdentifiedStorableDataAccess<RelationKey, D> {

    List<D> findWithFromClass(String className);

}
