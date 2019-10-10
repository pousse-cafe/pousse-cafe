package poussecafe.apm;

public class DefaultApmTransaction implements ApmTransaction {

    @Override
    public void captureException(Exception e) {
        // Do nothing
    }

    @Override
    public void end() {
        // Do nothing
    }
}
