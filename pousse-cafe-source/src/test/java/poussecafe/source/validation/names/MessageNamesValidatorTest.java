package poussecafe.source.validation.names;

@SuppressWarnings("squid:S2699")
public class MessageNamesValidatorTest extends NamesValidatorTest {

    @Override
    protected String componentClassName() {
        return "MyMessage";
    }

    @Override
    protected String componentFileName() {
        return "MyMessage.java";
    }
}
