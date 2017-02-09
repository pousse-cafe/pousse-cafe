package poussecafe.process;

import poussecafe.storable.ActiveStorableRepository;

public class ProcessManagerRepository
extends ActiveStorableRepository<ProcessManager, ProcessManagerKey, ProcessManager.Data> {

    @Override
    protected ProcessManager newStorable() {
        return new ProcessManager();
    }

    @Override
    protected void addData(ProcessManager processManager) {
        processManager.refreshStateMachineData();
        super.addData(processManager);
    }

    @Override
    protected void updateData(ProcessManager processManager) {
        processManager.refreshStateMachineData();
        super.updateData(processManager);
    }
}
