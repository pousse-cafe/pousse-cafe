package configuration;

import domain.MyAggregate;
import domain.MyFactory;
import domain.MyRepository;
import domain.data.MyAggregateData;
import java.util.Set;
import poussecafe.context.BoundedContext;
import poussecafe.domain.EntityImplementation;
import poussecafe.domain.EntityDefinition;
import poussecafe.domain.Service;
import poussecafe.process.DomainProcess;
import poussecafe.storage.memory.InMemoryDataAccess;
import poussecafe.storage.memory.InMemoryStorage;
import process.MyProcess;

public class MyMetaApplicationBundle extends BoundedContext {

    @Override
    protected void loadDefinitions(Set<EntityDefinition> definitions) {
        definitions.add(new EntityDefinition.Builder()
                .withEntityClass(MyAggregate.class)
                .withFactoryClass(MyFactory.class)
                .withRepositoryClass(MyRepository.class)
                .build());
    }

    @Override
    protected void loadImplementations(Set<EntityImplementation> implementations) {
        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(MyAggregate.class)
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
    protected void loadServices(Set<Class<? extends Service>> services) {
        // None in this example
    }

}
