package poussecafe.apm;

public interface ApmTransaction {

    void captureException(Exception e);

    void end();
}
