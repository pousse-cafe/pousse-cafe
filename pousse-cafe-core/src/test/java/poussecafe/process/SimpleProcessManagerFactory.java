package poussecafe.process;

public class SimpleProcessManagerFactory
extends ProcessManagerFactory<SimpleProcessManagerKey, SimpleProcessManager, SimpleProcessManager.Data> {

    @Override
    protected SimpleProcessManager newProcessManager() {
        return new SimpleProcessManager();
    }

}
