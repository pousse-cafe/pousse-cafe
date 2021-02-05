package poussecafe.source.validation.listener;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.Test;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.ValidatorTest;

@SuppressWarnings("squid:S2699")
public class MessageListenerValidatorTest extends ValidatorTest {

    @Test
    public void validContainerListenersNoMessage() throws IOException {
        givenValidator();
        givenMessageDefinitions();
        givenMessageImplementations();
        givenContainer();
        givenAggregateImplementation();
        givenRunner();
        whenValidating();
        thenNone(this::validContainerListenersMessage);
    }

    private void givenMessageDefinitions() {
        includeRelativeClass("Message1");
        includeRelativeClass("Message2");
        includeRelativeClass("Message3");
    }

    private void givenMessageImplementations() throws IOException {
        includeFile(path("Message1Data"));
        includeFile(path("Message2Data"));
        includeFile(path("Message3Data"));
    }

    private Path path(String string) {
        return Path.of("src", "test", "java", "poussecafe", "source", "validation", "listener", string + ".java");
    }

    private void givenContainer() throws IOException {
        includeFile(path("MyAggregate"));
    }

    private void givenAggregateImplementation() throws IOException {
        includeFile(path("MyAggregateDataAccess"));
    }

    private void givenRunner() throws IOException {
        includeFile(path("UpdatorRunner"));
    }

    private boolean validContainerListenersMessage(ValidationMessage message) {
        return message.location().source().id().endsWith("/MyAggregate.java")
                && (message.location().line() == 16
                        || message.location().line() == 24
                        || message.location().line() == 36);
    }

    @Test
    public void missingRunnerThenError() throws IOException {
        givenValidator();
        givenListenerWithoutRunner();
        whenValidating();
        thenAtLeast(this::missingRunnerError);
    }

    private void givenListenerWithoutRunner() throws IOException {
        includeFile(path("AggregateRootWithoutRunner"));
    }

    private boolean missingRunnerError(ValidationMessage message) {
        return message.location().source().id().endsWith("/AggregateRootWithoutRunner.java")
                && message.location().line() == 10
                && message.type() == ValidationMessageType.ERROR;
    }

    @Test
    public void wrongRunnerThenWarning() throws IOException {
        givenValidator();
        givenMessageDefinitions();
        givenMessageImplementations();
        givenListenerWithWrongRunner();
        givenWrongRunner();
        whenValidating();
        thenAtLeast(this::wrongRunnerWarning);
    }

    private void givenListenerWithWrongRunner() throws IOException {
        includeFile(path("AggregateRootWithWrongRunner"));
    }

    private void givenWrongRunner() throws IOException {
        includeFile(path("WrongRunner"));
    }

    private boolean wrongRunnerWarning(ValidationMessage message) {
        return message.location().source().id().endsWith("/WrongRunner.java")
                && message.location().line() == 5
                && message.type() == ValidationMessageType.WARNING;
    }
}
