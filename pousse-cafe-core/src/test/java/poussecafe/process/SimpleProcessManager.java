package poussecafe.process;

public class SimpleProcessManager extends ProcessManager<SimpleProcessManagerKey, SimpleProcessManager.Data> {

    public static interface Data extends ProcessManagerData<SimpleProcessManagerKey> {

    }
}
