package poussecafe.configuration;

import poussecafe.process.ProcessManager;
import poussecafe.process.ProcessManagerFactory;
import poussecafe.process.ProcessManagerKey;
import poussecafe.process.ProcessManagerRepository;

public class ProcessManagerConfiguration extends
ActiveStorableConfiguration<ProcessManagerKey, ProcessManager, ProcessManager.Data, ProcessManagerFactory, ProcessManagerRepository> {

    public ProcessManagerConfiguration() {
        super(ProcessManager.class, ProcessManager.Data.class, ProcessManagerFactory.class,
                ProcessManagerRepository.class);
    }

}
