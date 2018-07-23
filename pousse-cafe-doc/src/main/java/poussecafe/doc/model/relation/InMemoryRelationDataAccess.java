package poussecafe.doc.model.relation;

import java.util.List;
import poussecafe.storage.memory.InMemoryDataAccess;

import static java.util.stream.Collectors.toList;

public class InMemoryRelationDataAccess extends InMemoryDataAccess<RelationKey, RelationData> implements RelationDataAccess<RelationData> {

    @Override
    public List<RelationData> findWithFromClass(String className) {
        return findAll().stream().filter(data -> data.key().get().fromClass().equals(className)).collect(toList());
    }

}
