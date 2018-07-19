package poussecafe.doc.model.servicedoc;

import java.util.List;
import poussecafe.storage.memory.InMemoryDataAccess;

import static java.util.stream.Collectors.toList;

public class InMemoryServiceDocDataAccess extends InMemoryDataAccess<ServiceDocKey, ServiceDocData> implements ServiceDocDataAccess<ServiceDocData> {

    @Override
    public List<ServiceDocData> findByBoundedContextKey(String key) {
        return findAll().stream().filter(data -> data.key().get().boundedContextKey().equals(key)).collect(toList());
    }

}
