package poussecafe.source.validation.names;

@SuppressWarnings("squid:S2699")
public class EntityNamesValidatorTest extends NamesValidatorTest {

    @Override
    protected String componentClassName() {
        return "MyEntity";
    }

    @Override
    protected String componentFileName() {
        return "MyEntity.java";
    }
}
