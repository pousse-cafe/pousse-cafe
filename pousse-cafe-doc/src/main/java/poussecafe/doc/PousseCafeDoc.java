package poussecafe.doc;

import java.util.Set;
import poussecafe.context.BoundedContext;
import poussecafe.doc.model.AggregateDocLocator;
import poussecafe.doc.model.UbiquitousLanguageFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.aggregatedoc.AggregateDocData;
import poussecafe.doc.model.aggregatedoc.AggregateDocFactory;
import poussecafe.doc.model.aggregatedoc.AggregateDocRepository;
import poussecafe.doc.model.aggregatedoc.InMemoryAggregateDocDataAccess;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocData;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocFactory;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDocRepository;
import poussecafe.doc.model.boundedcontextdoc.InMemoryBoundedContextDocDataAccess;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.entitydoc.EntityDocData;
import poussecafe.doc.model.entitydoc.EntityDocFactory;
import poussecafe.doc.model.entitydoc.EntityDocRepository;
import poussecafe.doc.model.entitydoc.InMemoryEntityDocDataAccess;
import poussecafe.doc.model.servicedoc.InMemoryServiceDocDataAccess;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.doc.model.servicedoc.ServiceDocData;
import poussecafe.doc.model.servicedoc.ServiceDocFactory;
import poussecafe.doc.model.servicedoc.ServiceDocRepository;
import poussecafe.doc.process.AggregateDocCreation;
import poussecafe.doc.process.BoundedContextDocCreation;
import poussecafe.doc.process.EntityDocCreation;
import poussecafe.doc.process.ServiceDocCreation;
import poussecafe.domain.Service;
import poussecafe.process.DomainProcess;
import poussecafe.storable.StorableDefinition;
import poussecafe.storable.StorableImplementation;
import poussecafe.storage.memory.InMemoryStorage;

public class PousseCafeDoc extends BoundedContext {

    @Override
    protected void loadDefinitions(Set<StorableDefinition> definitions) {
        definitions.add(new StorableDefinition.Builder()
                .withStorableClass(BoundedContextDoc.class)
                .withFactoryClass(BoundedContextDocFactory.class)
                .withRepositoryClass(BoundedContextDocRepository.class)
                .build());

        definitions.add(new StorableDefinition.Builder()
                .withStorableClass(AggregateDoc.class)
                .withFactoryClass(AggregateDocFactory.class)
                .withRepositoryClass(AggregateDocRepository.class)
                .build());

        definitions.add(new StorableDefinition.Builder()
                .withStorableClass(ServiceDoc.class)
                .withFactoryClass(ServiceDocFactory.class)
                .withRepositoryClass(ServiceDocRepository.class)
                .build());

        definitions.add(new StorableDefinition.Builder()
                .withStorableClass(EntityDoc.class)
                .withFactoryClass(EntityDocFactory.class)
                .withRepositoryClass(EntityDocRepository.class)
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

        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(AggregateDoc.class)
                .withDataFactory(AggregateDocData::new)
                .withDataAccessFactory(InMemoryAggregateDocDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());

        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(ServiceDoc.class)
                .withDataFactory(ServiceDocData::new)
                .withDataAccessFactory(InMemoryServiceDocDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());

        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(EntityDoc.class)
                .withDataFactory(EntityDocData::new)
                .withDataAccessFactory(InMemoryEntityDocDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());
    }

    @Override
    protected void loadProcesses(Set<Class<? extends DomainProcess>> processes) {
        processes.add(BoundedContextDocCreation.class);
        processes.add(AggregateDocCreation.class);
        processes.add(ServiceDocCreation.class);
        processes.add(EntityDocCreation.class);
    }

    @Override
    protected void loadServices(Set<Class<? extends Service>> services) {
        services.add(GraphFactory.class);
        services.add(UbiquitousLanguageFactory.class);
        services.add(AggregateDocLocator.class);
    }

}
