package poussecafe.doc.model.relation;

import java.util.List;
import poussecafe.storage.internal.InternalDataAccess;

import static java.util.stream.Collectors.toList;

public class InternalRelationDataAccess extends InternalDataAccess<RelationKey, RelationData> implements RelationDataAccess<RelationData> {

    @Override
    public List<RelationData> findWithFromClass(String className) {
        return findAll().stream().filter(data -> data.key().get().fromClass().equals(className)).collect(toList());
    }

}
