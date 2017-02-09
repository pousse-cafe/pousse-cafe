package poussecafe.process;

public abstract class Init extends State {

    public static final String NAME = "Init";

    @Override
    public String getName() {
        return NAME;
    }

    public abstract void start();
}
