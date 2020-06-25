package poussecafe.runtime;

import org.junit.Test;
import poussecafe.collection.MapEditor;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class AttributesValidatorTest {

    @Test
    public void validMessagePassesWithAttributes() {
        givenAttributesWhenIgnoringDontCare(true);
        givenContainer(validContainer());
        whenValidating();
        thenValidationPassed(true);
    }

    private void givenAttributesWhenIgnoringDontCare(boolean ignore) {
        var attributes = new MapEditor<String, ValidationType>()
                .put("string", ValidationType.NOT_NULL)
                .put("optionalString", ValidationType.PRESENT)
                .put("dontCare", ignore ? ValidationType.NONE : ValidationType.NOT_NULL)
                .finish();
        validatorBuilder.attributes(attributes);
    }

    private AttributesValidator.Builder validatorBuilder = new AttributesValidator.Builder();

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
            validatorBuilder.build().validOrThrow(containerToValidate);
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
    public void invalidMessageFailsWithAttributes() {
        givenAttributesWhenIgnoringDontCare(true);
        givenContainer(invalidMessage());
        whenValidating();
        thenValidationPassed(false);
    }

    private Object invalidMessage() {
        return new AttributesContainer();
    }

    @Test
    public void messageWithMissingFieldFailsWithAttributes() {
        givenAttributesWhenIgnoringDontCare(false);
        givenContainer(validContainer());
        whenValidating();
        thenValidationPassed(false);
    }

    @Test
    public void validMessagePassesWithDefinition() {
        givenDefinition(AttributesDefinition.class);
        givenContainer(validContainerWithAnnotation());
        whenValidating();
        thenValidationPassed(true);
    }

    private Object validContainerWithAnnotation() {
        var validMessage = new AttributesContainerWithValidationAnnotation();
        validMessage.string().value("test");
        return validMessage;
    }

    private void givenDefinition(Class<?> definition) {
        validatorBuilder.definition(definition);
    }

    @Test
    public void invalidMessageFailsWithDefinition() {
        givenDefinition(AttributesDefinition.class);
        givenContainer(invalidMessageWithAnnotation());
        whenValidating();
        thenValidationPassed(false);
    }

    private Object invalidMessageWithAnnotation() {
        return new AttributesContainerWithValidationAnnotation();
    }

    @Test
    public void validMessageWithOverriddenFieldPassesWithDefinition() {
        givenDefinition(AttributesDefinition.class);
        givenContainer(validWithOverriddenField());
        whenValidating();
        thenValidationPassed(true);
    }

    private Object validWithOverriddenField() {
        var validMessage = new AttributesContainerWithOverriddenField();
        validMessage.string().value("test");
        return validMessage;
    }

    @Test
    public void validCombinedDefinitionAndContainerPasses() {
        givenDefinition(AttributesContainerWithValidationAnnotation.class);
        givenContainer(validContainerWithAnnotation());
        whenValidating();
        thenValidationPassed(true);
    }
}
