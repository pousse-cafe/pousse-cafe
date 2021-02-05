package poussecafe.source.validation.names;

import org.junit.Test;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.ValidatorTest;
import poussecafe.source.validation.names.components.Aggregate1;
import poussecafe.source.validation.names.components.Aggregate2;

@SuppressWarnings("squid:S2699")
public class ContainerAggregateRootsNamesValidatorTest extends ValidatorTest {

    @Test
    public void differentAggregatesWithContainerNoMessage() {
        givenValidator();
        givenAggregate1();
        givenAggregate2();
        whenValidating();
        thenNone(this::duplicateAggregate1Error);
        thenNone(this::duplicateAggregate2Error);
    }

    private void givenAggregate1() {
        includeClass(Aggregate1.class);
    }

    private void givenAggregate2() {
        includeClass(Aggregate2.class);
    }

    private boolean duplicateAggregate1Error(ValidationMessage message) {
        return message.location().source().id().endsWith("/components/Aggregate1.java")
                && message.message().contains("same name already exists")
                && message.type() == ValidationMessageType.ERROR;
    }

    private boolean duplicateAggregate2Error(ValidationMessage message) {
        return message.location().source().id().endsWith("/components/Aggregate2.java")
                && message.message().contains("same name already exists")
                && message.type() == ValidationMessageType.ERROR;
    }

    @Test
    public void sameAggregateWithContainerError() {
        givenValidator();
        givenAggregate1();
        givenDuplicateAggregate1();
        whenValidating();
        thenAtLeast(this::duplicateAggregate1Error);
        thenAtLeast(this::duplicateAggregate1DuplicateError);
    }

    private void givenDuplicateAggregate1() {
        includeClass(poussecafe.source.validation.names.components2.Aggregate1.class);
    }

    private boolean duplicateAggregate1DuplicateError(ValidationMessage message) {
        return message.location().source().id().endsWith("/components2/Aggregate1.java")
                && message.message().contains("same name already exists")
                && message.type() == ValidationMessageType.ERROR;
    }
}
