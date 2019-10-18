package poussecafe.apm;

public class DefaultApmTransaction implements ApmTransaction {

    public static DefaultApmTransaction instance() {
        return SINGLETON;
    }

    private static final DefaultApmTransaction SINGLETON = new DefaultApmTransaction();

    private DefaultApmTransaction() {

    }

    @Override
    public void captureException(Throwable e) {
        // Do nothing
    }

    @Override
    public void end() {
        // Do nothing
    }

    @Override
    public void setResult(String result) {
        // Do nothing
    }

    @Override
    public void addLabel(String key, String value) {
        // Do nothing
    }

    @Override
    public void setName(String name) {
        // Do nothing
    }

    @Override
    public ApmSpan startSpan() {
        return SINGLETON;
    }
}
