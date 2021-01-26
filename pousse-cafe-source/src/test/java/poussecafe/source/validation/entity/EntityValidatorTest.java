package poussecafe.source.validation.entity;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.Test;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.ValidatorTest;

@SuppressWarnings("squid:S2699") // Assertions in parent class
public class EntityValidatorTest extends ValidatorTest {

    @Test
    public void entityWithImplementationNoMessage() throws IOException {
        givenValidator();
        givenDefinition();
        givenImplementation();
        whenValidating();
        thenNone(this::missingImplementationWarning);
    }

    private void givenDefinition() throws IOException {
        includeFile(entityDefinitionSourcePath());
    }

    private Path entityDefinitionSourcePath() {
        return Path.of("src", "test", "java", "poussecafe", "source", "validation", "entity", "MyEntity.java");
    }

    private void givenImplementation() throws IOException {
        includeFile(Path.of("src", "test", "java", "poussecafe", "source", "validation", "entity", "adapters", "MyEntityAttributes.java"));
    }

    @Test
    public void entityWithoutImplementationWarning() throws IOException {
        givenValidator();
        givenDefinition();
        whenValidating();
        thenAtLeast(this::missingImplementationWarning);
    }

    private boolean missingImplementationWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
            && message.location().sourceFile().id().equals(entityDefinitionSourcePath().toString())
            && message.location().line() == 6
            && message.message().contains("No implementation");
    }

    @Test
    public void aggregateRootWithDataAccessNoMessage() throws IOException {
        givenValidator();
        givenAggregateRootDefinition();
        givenAggregateRootImplementation();
        givenDataAccess();
        whenValidating();
        thenNone(this::missingAggregateRootImplementationWarning);
    }

    private void givenAggregateRootDefinition() throws IOException {
        includeFile(Path.of("src", "test", "java", "poussecafe", "source", "validation", "entity", "MyAggregateRoot.java"));
    }

    private void givenAggregateRootImplementation() throws IOException {
        includeFile(Path.of("src", "test", "java", "poussecafe", "source", "validation", "entity", "adapters", "MyAggregateRootAttributes.java"));
    }

    private void givenDataAccess() throws IOException {
        includeFile(Path.of("src", "test", "java", "poussecafe", "source", "validation", "entity", "adapters", "MyEntityDataAccess.java"));
    }

    private boolean missingAggregateRootImplementationWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
            && message.location().sourceFile().id().endsWith("/MyAggregateRoot.java")
            && message.location().line() == 6
            && message.message().contains("No implementation");
    }

    @Test
    public void aggregateRootWithSeveralDataAccessesNoMessage() throws IOException {
        givenValidator();
        givenAggregateRootDefinition();
        givenAggregateRootImplementation();
        givenDataAccess();
        givenDataAccess2();
        whenValidating();
        thenNone(this::missingAggregateRootImplementationWarning);
    }

    private void givenDataAccess2() throws IOException {
        includeFile(Path.of("src", "test", "java", "poussecafe", "source", "validation", "entity", "adapters", "MyEntityDataAccess2.java"));
    }
}
