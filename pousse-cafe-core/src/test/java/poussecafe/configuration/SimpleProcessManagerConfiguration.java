package poussecafe.configuration;

import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryDataFactory;
import poussecafe.process.SimpleProcessManager;
import poussecafe.process.SimpleProcessManager.Data;
import poussecafe.process.SimpleProcessManagerFactory;
import poussecafe.process.SimpleProcessManagerKey;
import poussecafe.process.SimpleProcessManagerRepository;

public class SimpleProcessManagerConfiguration extends
ProcessManagerConfiguration<SimpleProcessManagerKey, SimpleProcessManager, SimpleProcessManager.Data, SimpleProcessManagerFactory, SimpleProcessManagerRepository> {

    public SimpleProcessManagerConfiguration() {
        super(SimpleProcessManager.class, SimpleProcessManagerFactory.class, SimpleProcessManagerRepository.class);
        setDataFactory(new InMemoryDataFactory<>(Data.class));
        setDataAccess(new InMemoryDataAccess<>(Data.class));
    }

}
