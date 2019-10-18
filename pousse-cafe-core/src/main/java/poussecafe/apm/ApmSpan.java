package poussecafe.apm;

public interface ApmSpan {

    void setName(String name);

    void captureException(Throwable e);

    void end();

    ApmSpan startSpan();
}
