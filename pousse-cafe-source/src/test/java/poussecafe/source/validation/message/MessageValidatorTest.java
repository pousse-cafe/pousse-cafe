package poussecafe.source.validation.message;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.Test;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.ValidatorTest;

@SuppressWarnings("squid:S2699")
public class MessageValidatorTest extends ValidatorTest {

    @Test
    public void messageWithImplementationLeadsNoMessage() throws IOException {
        givenValidator();
        givenDefinition();
        givenImplementation();
        whenValidating();
        thenNone(this::missingImplementationError);
    }

    private void givenDefinition() throws IOException {
        validator.includeFile(messageDefinitionSourcePath());
    }

    private Path messageDefinitionSourcePath() {
        return Path.of("src", "test", "java", "poussecafe","source","validation", "message", "Message1.java");
    }

    private void givenImplementation() throws IOException {
        validator.includeFile(Path.of("src", "test", "java", "poussecafe", "source", "validation", "message", "Message1Data.java"));
    }

    private boolean missingImplementationError(ValidationMessage message) {
        return message.type() == ValidationMessageType.ERROR
                && message.message().contains("No implementation");
    }

    @Test
    public void messageWithoutImplementationLeadsError() throws IOException {
        givenValidator();
        givenDefinition();
        whenValidating();
        thenAtLeast(this::missingImplementationError);
    }

    @Test
    public void autoImplementedCommandNoWarning() throws IOException {
        givenValidator();
        givenAutoImplementedCommand();
        whenValidating();
        thenNone(this::missingImplementationError);
    }

    private void givenAutoImplementedCommand() {
        includeClass(AutoImplementedCommand.class);
    }

    @Test
    public void autoImplementedEventWarning() throws IOException {
        givenValidator();
        givenAutoImplementedEvent();
        whenValidating();
        thenAtLeast(this::autoImplementedEventWarning);
    }

    private void givenAutoImplementedEvent() {
        includeClass(AutoImplementedEvent.class);
    }

    private boolean autoImplementedEventWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
                && message.location().sourceFile().id().endsWith("/AutoImplementedEvent.java")
                && message.message().contains("should not implement itself");
    }
}
