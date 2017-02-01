package poussecafe.process;

import poussecafe.storable.ActiveStorableFactory;

public abstract class ProcessManagerFactory<K, A extends ProcessManager<K, D>, D extends ProcessManagerData<K>>
extends ActiveStorableFactory<K, A, D> {

    @Override
    protected A newStorable() {
        return newProcessManager();
    }

    protected abstract A newProcessManager();

}
