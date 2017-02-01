package poussecafe.process;

import poussecafe.storable.ActiveStorableRepository;

public abstract class ProcessManagerRepository<K, D extends ProcessManagerData<K>, P extends ProcessManager<K, D>>
extends ActiveStorableRepository<P, K, D> {

    @Override
    protected P newStorable() {
        return newProcessManager();
    }

    protected abstract P newProcessManager();

}
