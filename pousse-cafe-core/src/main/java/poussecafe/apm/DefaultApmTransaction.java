package poussecafe.apm;

public class DefaultApmTransaction implements ApmTransaction {

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
}
