package poussecafe.process;

public class SimpleProcessManagerRepository
extends ProcessManagerRepository<SimpleProcessManagerKey, SimpleProcessManager.Data, SimpleProcessManager> {

    @Override
    protected SimpleProcessManager newProcessManager() {
        return new SimpleProcessManager();
    }

}
