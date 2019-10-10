package poussecafe.apm;

public interface ApmTransaction {

    void captureException(Throwable e);

    void end();
}
