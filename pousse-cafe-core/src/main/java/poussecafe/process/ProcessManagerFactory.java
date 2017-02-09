package poussecafe.process;

import poussecafe.storable.ActiveStorableFactory;

public class ProcessManagerFactory
extends ActiveStorableFactory<ProcessManagerKey, ProcessManager, ProcessManager.Data> {

    @Override
    protected ProcessManager newStorable() {
        return new ProcessManager();
    }

    public ProcessManager buildWithInitialState(ProcessManagerKey key,
            StateMachine stateMachine) {
        ProcessManager processManager = newStorableWithKey(key);
        processManager.setStateMachine(stateMachine);
        return processManager;
    }

}
