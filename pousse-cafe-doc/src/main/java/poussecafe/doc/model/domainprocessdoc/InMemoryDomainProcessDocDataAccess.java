package poussecafe.doc.model.domainprocessdoc;

import java.util.List;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocKey;
import poussecafe.storage.memory.InMemoryDataAccess;

import static java.util.stream.Collectors.toList;

public class InMemoryDomainProcessDocDataAccess extends InMemoryDataAccess<DomainProcessDocKey, DomainProcessDocData> implements DomainProcessDocDataAccess<DomainProcessDocData> {

    @Override
    public List<DomainProcessDocData> findByBoundedContextKey(BoundedContextDocKey key) {
        return findAll().stream().filter(data -> data.boundedContextComponentDoc().get().boundedContextDocKey().equals(key)).collect(toList());
    }
}
