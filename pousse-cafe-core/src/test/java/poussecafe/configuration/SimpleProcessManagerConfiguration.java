package poussecafe.configuration;

import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryStorableDataFactory;
import poussecafe.process.SimpleProcessManager;
import poussecafe.process.SimpleProcessManager.Data;
import poussecafe.process.SimpleProcessManagerFactory;
import poussecafe.process.SimpleProcessManagerKey;
import poussecafe.process.SimpleProcessManagerRepository;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataFactory;

public class SimpleProcessManagerConfiguration extends
ProcessManagerConfiguration<SimpleProcessManagerKey, SimpleProcessManager, SimpleProcessManager.Data, SimpleProcessManagerFactory, SimpleProcessManagerRepository> {

    public SimpleProcessManagerConfiguration() {
        super(SimpleProcessManager.class, SimpleProcessManagerFactory.class, SimpleProcessManagerRepository.class);
    }

    @Override
    protected StorableDataFactory<Data> aggregateDataFactory() {
        return new InMemoryStorableDataFactory<>(Data.class);
    }

    @Override
    protected StorableDataAccess<SimpleProcessManagerKey, Data> dataAccess() {
        return new InMemoryDataAccess<>(Data.class);
    }

}
