package poussecafe.process;

public abstract class ErrorState extends State {

    public static final String NAME = "Error";

    @Override
    public String getName() {
        return NAME;
    }

    public abstract String getErrorDescription();
}
