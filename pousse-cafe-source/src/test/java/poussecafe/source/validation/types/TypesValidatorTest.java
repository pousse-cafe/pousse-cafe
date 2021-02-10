package poussecafe.source.validation.types;

import org.junit.Test;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.ValidatorTest;
import poussecafe.source.validation.types.adapters.MyAggregateAttributes;
import poussecafe.source.validation.types.adapters.MyAggregateInternalDataAccess;

@SuppressWarnings("squid:S2699")
public class TypesValidatorTest extends ValidatorTest {

    @Test
    public void warnNoDataAccessDefinition() {
        givenValidator();
        givenAggregate();
        whenValidating();
        thenAtLeast(this::noDataAccessDefinitionWarning);
    }

    private void givenAggregate() {
        includeClass(MyAggregate.class);
    }

    private boolean noDataAccessDefinitionWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
                && message.location().source().id().endsWith("/MyAggregate.java")
                && message.message().contains("Data access definition missing");
    }

    @Test
    public void noMessageWithDataAccessDefinition() {
        givenValidator();
        givenAggregate();
        givenDataAccessDefinition();
        whenValidating();
        thenNone(this::noDataAccessDefinitionWarning);
    }

    private void givenDataAccessDefinition() {
        includeClass(MyAggregateDataAccess.class);
    }

    @Test
    public void warnNoAttributesImplementation() {
        givenValidator();
        givenAggregate();
        whenValidating();
        thenAtLeast(this::noAttributesImplementationWarning);
    }

    private boolean noAttributesImplementationWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
                && message.location().source().id().endsWith("/MyAggregate.java")
                && message.message().contains("Attributes implementation missing");
    }

    @Test
    public void noMessageWithAttributesImplementation() {
        givenValidator();
        givenAggregate();
        givenAttributesImplementation();
        whenValidating();
        thenNone(this::noAttributesImplementationWarning);
    }

    private void givenAttributesImplementation() {
        includeClass(MyAggregateAttributes.class);
    }

    @Test
    public void warnNoDataAccessImplementation() {
        givenValidator();
        givenAggregate();
        whenValidating();
        thenAtLeast(this::noDataAccessImplementationWarning);
    }

    private boolean noDataAccessImplementationWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
                && message.location().source().id().endsWith("/MyAggregate.java")
                && message.message().contains("Data access implementation for storage Internal missing");
    }

    @Test
    public void noMessageWithDataAccessImplementation() {
        givenValidator();
        givenAggregate();
        givenDataAccessImplementation();
        whenValidating();
        thenNone(this::noDataAccessImplementationWarning);
    }

    private void givenDataAccessImplementation() {
        includeClass(MyAggregateInternalDataAccess.class);
    }
}
