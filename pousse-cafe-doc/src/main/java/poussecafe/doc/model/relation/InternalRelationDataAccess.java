package poussecafe.doc.model.relation;

import java.util.List;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import static java.util.stream.Collectors.toList;

@DataAccessImplementation(
    aggregateRoot = Relation.class,
    dataImplementation = RelationData.class,
    storageName = InternalStorage.NAME
)
public class InternalRelationDataAccess extends InternalDataAccess<RelationKey, RelationData> implements RelationDataAccess<RelationData> {

    @Override
    public List<RelationData> findWithFromClass(String className) {
        return findAll().stream().filter(data -> data.key().value().fromClass().equals(className)).collect(toList());
    }

}
