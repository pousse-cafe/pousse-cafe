package poussecafe.doc;

import java.util.Set;
import poussecafe.context.MetaApplicationBundle;
import poussecafe.doc.data.BoundedContextDocData;
import poussecafe.doc.data.InMemoryBoundedContextDocDataAccess;
import poussecafe.doc.model.BoundedContextDoc;
import poussecafe.doc.model.BoundedContextDocFactory;
import poussecafe.doc.model.BoundedContextDocRepository;
import poussecafe.doc.process.BoundedContextDocCreation;
import poussecafe.process.DomainProcess;
import poussecafe.storable.StorableDefinition;
import poussecafe.storable.StorableImplementation;
import poussecafe.storage.memory.InMemoryStorage;

public class PousseCafeDoc extends MetaApplicationBundle {

    @Override
    protected void loadDefinitions(Set<StorableDefinition> definitions) {
        definitions.add(new StorableDefinition.Builder()
                .withStorableClass(BoundedContextDoc.class)
                .withFactoryClass(BoundedContextDocFactory.class)
                .withRepositoryClass(BoundedContextDocRepository.class)
                .build());
    }

    @Override
    protected void loadImplementations(Set<StorableImplementation> implementations) {
        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(BoundedContextDoc.class)
                .withDataFactory(BoundedContextDocData::new)
                .withDataAccessFactory(InMemoryBoundedContextDocDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());
    }

    @Override
    protected void loadProcesses(Set<Class<? extends DomainProcess>> processes) {
        processes.add(BoundedContextDocCreation.class);
    }

    @Override
    protected void loadServices(Set<Class<?>> services) {

    }

}
