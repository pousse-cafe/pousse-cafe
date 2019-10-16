package poussecafe.apm;

public interface ApmTransaction {

    void captureException(Throwable e);

    void end();

    void setResult(String result);

    void addLabel(String key, String value);
}
