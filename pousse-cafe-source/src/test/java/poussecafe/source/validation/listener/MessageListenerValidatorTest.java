package poussecafe.source.validation.listener;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.Test;
import poussecafe.source.validation.ValidatorTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("squid:S2699")
public class MessageListenerValidatorTest extends ValidatorTest {

    @Test
    public void validContainerListenersNoMessage() throws IOException {
        givenValidator();
        givenMessageDefinitions();
        givenMessageImplementations();
        givenContainer();
        givenAggregateImplementation();
        whenValidating();
        thenNoMessage();
    }

    private void givenMessageDefinitions() throws IOException {
        validator.includeFile(path("Message1"));
        validator.includeFile(path("Message2"));
        validator.includeFile(path("Message3"));
    }

    private void givenMessageImplementations() throws IOException {
        validator.includeFile(path("Message1Data"));
        validator.includeFile(path("Message2Data"));
        validator.includeFile(path("Message3Data"));
    }

    private Path path(String string) {
        return Path.of("src", "test", "java", "poussecafe", "source", "validation", "listener", string + ".java");
    }

    private void givenContainer() throws IOException {
        validator.includeFile(path("MyAggregate"));
    }

    private void givenAggregateImplementation() throws IOException {
        validator.includeFile(path("MyAggregateDataAccess"));
    }

    @Test
    public void missingMessageDefinitionError() throws IOException {
        givenValidator();
        givenContainer();
        givenAggregateImplementation();
        whenValidating();
        thenMissingDefinitionsMessages();
    }

    private void thenMissingDefinitionsMessages() {
        assertThat(result.messages().size(), is(3));
        var expectedMessage = "A message listener must consume a message i.e. have a message definition type as single parameter's type";
        assertTrue(result.messages().stream().allMatch(message -> message.message().equals(expectedMessage)));
        var fileId = path("MyAggregate").toString();
        assertTrue(result.messages().stream().allMatch(message -> message.location().sourceFile().id().equals(fileId)));
        assertTrue(result.messages().stream().anyMatch(message -> message.location().line() == 16));
        assertTrue(result.messages().stream().anyMatch(message -> message.location().line() == 24));
        assertTrue(result.messages().stream().anyMatch(message -> message.location().line() == 36));
    }
}