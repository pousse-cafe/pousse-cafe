package poussecafe.service;

import java.util.function.Function;
import poussecafe.process.ProcessManager;
import poussecafe.process.ProcessManagerFactory;
import poussecafe.process.ProcessManagerKey;
import poussecafe.process.ProcessManagerRepository;
import poussecafe.process.State;
import poussecafe.process.StateMachine;

public abstract class Workflow extends TransactionAwareService {

    private ProcessManagerFactory processManagerFactory;

    private ProcessManagerRepository processManagerRepository;

    protected ProcessManagerKey startProcess(ProcessManagerKey processManagerKey,
            StateMachine newStateMachine) {
        ProcessManager processManager = processManagerFactory.buildWithInitialState(processManagerKey, newStateMachine);
        newStateMachine.start();
        runInTransaction(() -> processManagerRepository.add(processManager));
        return processManagerKey;
    }

    protected void executeTransition(ProcessManagerKey processManagerKey,
            Function<State, State> transitionChooser) {
        runInTransaction(() -> {
            ProcessManager processManager = processManagerRepository.get(processManagerKey);
            processManager.getStateMachine().executeTransition(transitionChooser);
            processManagerRepository.update(processManager);
        });
    }

    public void setProcessManagerFactory(ProcessManagerFactory processManagerFactory) {
        this.processManagerFactory = processManagerFactory;
    }

    public void setProcessManagerRepository(ProcessManagerRepository processManagerRepository) {
        this.processManagerRepository = processManagerRepository;
    }
}
