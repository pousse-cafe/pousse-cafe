package poussecafe.source.validation.names;

@SuppressWarnings("squid:S2699")
public class ProcessNamesValidatorTest extends NamesValidatorTest {

    @Override
    protected String componentClassName() {
        return "MyProcess";
    }

    @Override
    protected String componentFileName() {
        return "MyProcess.java";
    }
}
