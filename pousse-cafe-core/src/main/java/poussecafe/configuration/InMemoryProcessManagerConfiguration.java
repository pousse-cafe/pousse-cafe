package poussecafe.configuration;

import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryDataFactory;
import poussecafe.process.ProcessManager;

public class InMemoryProcessManagerConfiguration extends ProcessManagerConfiguration {

    public InMemoryProcessManagerConfiguration() {
        setDataAccess(new InMemoryDataAccess<>(ProcessManager.Data.class));
        setDataFactory(new InMemoryDataFactory<>(ProcessManager.Data.class));
    }
}
