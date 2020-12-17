package poussecafe.source.validation;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.Test;
import poussecafe.source.analysis.ClassLoaderClassResolver;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class ValidatorTest {

    @Test
    public void messageWithImplementationLeadsNoMessage() throws IOException {
        givenValidator();
        givenDefinition();
        givenImplementation();
        whenValidating();
        thenNoMessage();
    }

    private void givenValidator() {
        validator = new Validator(new ClassLoaderClassResolver());
    }

    private void givenDefinition() throws IOException {
        validator.includeFile(messageDefinitionSourcePath());
    }

    private Path messageDefinitionSourcePath() {
        return Path.of("src", "test", "java", "poussecafe","source","validation","Message1.java");
    }

    private void givenImplementation() throws IOException {
        validator.includeFile(Path.of("src", "test", "java", "poussecafe","source","validation","Message1Data.java"));
    }

    private void whenValidating() {
        validator.validate();
        result = validator.result();
    }

    private Validator validator;

    private ValidationResult result;

    private void thenNoMessage() {
        assertTrue(result.messages().isEmpty());
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
