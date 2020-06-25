package poussecafe.runtime;

import java.util.Map;
import org.junit.Test;
import poussecafe.collection.MapEditor;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class AttributesValidatorTest {

    @Test
    public void validMessagePasses() {
        givenDefinitionWhenIgnoringDontCare(true);
        givenContainer(validContainer());
        whenValidating();
        thenValidationPassed(true);
    }

    private void givenDefinitionWhenIgnoringDontCare(boolean ignore) {
        attributes = new MapEditor<String, ValidationType>()
                .put("string", ValidationType.NOT_NULL)
                .put("optionalString", ValidationType.PRESENT)
                .put("dontCare", ignore ? ValidationType.NONE : ValidationType.NOT_NULL)
                .finish();
    }

    private Map<String, ValidationType> attributes;

    private void givenContainer(Object validContainer) {
        containerToValidate = validContainer;
    }

    private Object containerToValidate;

    private Object validContainer() {
        var validMessage = new AttributesContainer();
        validMessage.string().value("test");
        return validMessage;
    }

    private void whenValidating() {
        try {
            new AttributesValidator.Builder()
                .attributes(attributes)
                .build()
                .validOrThrow(containerToValidate);
        } catch(IllegalArgumentException e) {
            validationException = e;
        }
    }

    private void thenValidationPassed(boolean success) {
        if(success) {
            assertThat(validationException, nullValue());
        } else {
            assertThat(validationException, notNullValue());
        }
    }

    private Exception validationException;

    @Test
    public void invvalidMessageFails() {
        givenDefinitionWhenIgnoringDontCare(true);
        givenContainer(invalidMessage());
        whenValidating();
        thenValidationPassed(false);
    }

    private Object invalidMessage() {
        return new AttributesContainer();
    }

    @Test
    public void messageWithMissingFieldFails() {
        givenDefinitionWhenIgnoringDontCare(false);
        givenContainer(validContainer());
        whenValidating();
        thenValidationPassed(false);
    }
}
