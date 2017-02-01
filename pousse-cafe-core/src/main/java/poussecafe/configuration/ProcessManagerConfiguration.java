package poussecafe.configuration;

import poussecafe.process.ProcessManager;
import poussecafe.process.ProcessManagerData;
import poussecafe.process.ProcessManagerFactory;
import poussecafe.process.ProcessManagerRepository;

public abstract class ProcessManagerConfiguration<K, P extends ProcessManager<K, D>, D extends ProcessManagerData<K>, F extends ProcessManagerFactory<K, P, D>, R extends ProcessManagerRepository<K, D, P>>
extends ActiveStorableConfiguration<K, P, D, F, R> {

    public ProcessManagerConfiguration(Class<P> processManagerClass, Class<F> factoryClass, Class<R> repositoryClass) {
        super(processManagerClass, factoryClass, repositoryClass);
    }

    public ProcessManagerConfiguration(Class<P> processManagerClass, StorableServiceFactory<F, R> serviceFactory) {
        super(processManagerClass, serviceFactory);
    }

}
