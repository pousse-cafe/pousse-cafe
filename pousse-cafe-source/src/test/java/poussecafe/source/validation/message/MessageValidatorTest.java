package poussecafe.source.validation.message;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.Test;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.ValidatorTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("squid:S2699")
public class MessageValidatorTest extends ValidatorTest {

    @Test
    public void messageWithImplementationLeadsNoMessage() throws IOException {
        givenValidator();
        givenDefinition();
        givenImplementation();
        whenValidating();
        thenNoMessage();
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

    @Test
    public void messageWithoutImplementationLeadsWarning() throws IOException {
        givenValidator();
        givenDefinition();
        whenValidating();
        thenNoImplementationWarning();
    }

    private void thenNoImplementationWarning() {
        assertThat(result.messages().size(), is(1));
        var message = result.messages().get(0);
        assertThat(message.type(), is(ValidationMessageType.WARNING));
        assertThat(message.location().sourceFile().id(), equalTo(messageDefinitionSourcePath().toString()));
        assertThat(message.location().line(), is(5));
    }
}
