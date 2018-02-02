package configuration;

import domain.MyAggregate;
import domain.MyFactory;
import domain.MyRepository;
import domain.data.MyAggregateData;
import java.util.Set;
import poussecafe.context.MetaApplicationBundle;
import poussecafe.process.DomainProcess;
import poussecafe.storable.StorableDefinition;
import poussecafe.storable.StorableImplementation;
import poussecafe.storage.memory.InMemoryDataAccess;
import poussecafe.storage.memory.InMemoryStorage;
import process.MyProcess;

public class MyMetaApplicationBundle extends MetaApplicationBundle {

    @Override
    protected void loadDefinitions(Set<StorableDefinition> definitions) {
        definitions.add(new StorableDefinition.Builder()
                .withStorableClass(MyAggregate.class)
                .withFactoryClass(MyFactory.class)
                .withRepositoryClass(MyRepository.class)
                .build());
    }

    @Override
    protected void loadImplementations(Set<StorableImplementation> implementations) {
        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(MyAggregate.class)
                .withDataFactory(MyAggregateData::new)
                .withDataAccessFactory(InMemoryDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());
    }

    @Override
    protected void loadProcesses(Set<Class<? extends DomainProcess>> processes) {
        processes.add(MyProcess.class);
    }

    @Override
    protected void loadServices(Set<Class<?>> services) {
        // None in this example
    }

}
