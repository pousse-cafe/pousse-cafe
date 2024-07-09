package poussecafe.runtime;

public class ProcessingMode {

    public static ProcessingMode synchronous() {
        var mode = new ProcessingMode();
        mode.synchronous = true;
        mode.processingThreads = 0;
        return mode;
    }

    public static ProcessingMode threadPool(int processingThreads) {
        var mode = new ProcessingMode();
        mode.synchronous = false;
        mode.processingThreads = processingThreads;
        return mode;
    }

    public boolean isSynchronous() {
        return synchronous;
    }

    private boolean synchronous;

    public int processingThreads() {
        return processingThreads;
    }

    private int processingThreads;

    private ProcessingMode() {
        
    }
}
