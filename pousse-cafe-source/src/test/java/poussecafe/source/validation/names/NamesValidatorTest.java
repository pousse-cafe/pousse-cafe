package poussecafe.source.validation.names;

import org.junit.Test;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.ValidatorTest;

@SuppressWarnings("squid:S2699")
public abstract class NamesValidatorTest extends ValidatorTest {

    @Test
    public void uniqueMessageNameNoMessage() {
        givenValidator();
        givenComponent();
        whenValidating();
        thenNone(this::duplicateMessageError);
    }

    private void givenComponent() {
        includeRelativeClass("components", componentClassName());
    }

    protected abstract String componentClassName();

    private boolean duplicateMessageError(ValidationMessage message) {
        return message.location().sourceFile().id().endsWith("/components/" + componentFileName())
                && message.message().contains("same name already exists")
                && message.type() == ValidationMessageType.ERROR;
    }

    protected abstract String componentFileName();

    @Test
    public void duplicateMessageNameError() {
        givenValidator();
        givenComponent();
        givenComponent2();
        whenValidating();
        thenAtLeast(this::duplicateMessageError);
        thenAtLeast(this::duplicateMessage2Error);
    }

    private void givenComponent2() {
        includeRelativeClass("components2", componentClassName());
    }

    private boolean duplicateMessage2Error(ValidationMessage message) {
        return message.location().sourceFile().id().endsWith("/components2/" + componentFileName())
                && message.message().contains("same name already exists")
                && message.type() == ValidationMessageType.ERROR;
    }
}
