package poussecafe.source.validation.namingconventions;

import org.junit.Test;
import poussecafe.source.validation.ValidationMessage;
import poussecafe.source.validation.ValidationMessageType;
import poussecafe.source.validation.ValidatorTest;

@SuppressWarnings("squid:S2699")
public class NamingConventionsValidatorTest extends ValidatorTest {

    @Test
    public void correctStandaloneAggregateRootName() {
        givenValidator();
        givenStandaloneRoot();
        whenValidating();
        thenNone(this::standloneAggregateRootNameWarning);
    }

    private void givenStandaloneRoot() {
        includeClass(StandaloneAggregateRoot.class);
    }

    private boolean standloneAggregateRootNameWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
                && message.location().source().id().contains("/StandaloneAggregateRoot")
                && message.message().contains("does not follow naming convention");
    }

    @Test
    public void warnWrongStandaloneAggregateRootName() {
        givenValidator();
        givenStandaloneRoot2();
        whenValidating();
        thenAtLeast(this::standloneAggregateRootNameWarning);
    }

    private void givenStandaloneRoot2() {
        includeClass(StandaloneAggregateRoot2.class);
    }

    @Test
    public void correctInnerAggregateRootName() {
        givenValidator();
        givenAggregate();
        whenValidating();
        thenNone(this::innerAggregateRootNameWarning);
    }

    private void givenAggregate() {
        includeClass(MyAggregate.class);
    }

    private boolean innerAggregateRootNameWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
                && message.location().source().id().contains("/MyAggregate")
                && message.message().contains("Aggregate root name does not follow naming convention");
    }

    @Test
    public void warnWrongInnerAggregateRootName() {
        givenValidator();
        givenAggregate2();
        whenValidating();
        thenAtLeast(this::innerAggregateRootNameWarning);
    }

    private void givenAggregate2() {
        includeClass(MyAggregate2.class);
    }

    @Test
    public void correctStandaloneAggregateFactoryName() {
        givenValidator();
        givenStandaloneFactory();
        whenValidating();
        thenNone(this::standloneAggregateFactoryNameWarning);
    }

    private void givenStandaloneFactory() {
        includeClass(StandaloneFactory.class);
    }

    private boolean standloneAggregateFactoryNameWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
                && message.location().source().id().contains("/StandaloneFactory")
                && message.message().contains("does not follow naming convention");
    }

    @Test
    public void warnWrongStandaloneAggregateFactoryName() {
        givenValidator();
        givenStandaloneFactory2();
        whenValidating();
        thenAtLeast(this::standloneAggregateFactoryNameWarning);
    }

    private void givenStandaloneFactory2() {
        includeClass(StandaloneFactory2.class);
    }

    @Test
    public void correctInnerAggregateFactoryName() {
        givenValidator();
        givenAggregate();
        whenValidating();
        thenNone(this::innerAggregateFactoryNameWarning);
    }

    private boolean innerAggregateFactoryNameWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
                && message.location().source().id().contains("/MyAggregate")
                && message.message().contains("Aggregate factory name does not follow naming convention");
    }

    @Test
    public void warnWrongInnerAggregateFactoryName() {
        givenValidator();
        givenAggregate2();
        whenValidating();
        thenAtLeast(this::innerAggregateFactoryNameWarning);
    }

    @Test
    public void correctStandaloneAggregateRepositoryName() {
        givenValidator();
        givenStandaloneRepository();
        whenValidating();
        thenNone(this::standloneAggregateRepositoryNameWarning);
    }

    private void givenStandaloneRepository() {
        includeClass(StandaloneRepository.class);
    }

    private boolean standloneAggregateRepositoryNameWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
                && message.location().source().id().contains("/StandaloneRepository")
                && message.message().contains("does not follow naming convention");
    }

    @Test
    public void warnWrongStandaloneAggregateRepositoryName() {
        givenValidator();
        givenStandaloneRepository2();
        whenValidating();
        thenAtLeast(this::standloneAggregateRepositoryNameWarning);
    }

    private void givenStandaloneRepository2() {
        includeClass(StandaloneRepository2.class);
    }

    @Test
    public void correctInnerAggregateRepositoryName() {
        givenValidator();
        givenAggregate();
        whenValidating();
        thenNone(this::innerAggregateRepositoryNameWarning);
    }

    private boolean innerAggregateRepositoryNameWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
                && message.location().source().id().contains("/MyAggregate")
                && message.message().contains("Aggregate repository name does not follow naming convention");
    }

    @Test
    public void warnWrongInnerAggregateRepositoryName() {
        givenValidator();
        givenAggregate2();
        whenValidating();
        thenAtLeast(this::innerAggregateRepositoryNameWarning);
    }

    @Test
    public void correctDataAccessName() {
        givenValidator();
        givenAggregateDataAccess();
        whenValidating();
        thenNone(this::dataAccessNameWarning);
    }

    private void givenAggregateDataAccess() {
        includeClass(MyAggregateDataAccess.class);
    }

    private boolean dataAccessNameWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
                && message.location().source().id().contains("/MyAggregateDataAccess")
                && message.message().contains("Data access definition name does not follow naming convention");
    }

    @Test
    public void warnWrongDataAccessName() {
        givenValidator();
        givenAggregateDataAccess2();
        whenValidating();
        thenAtLeast(this::dataAccessNameWarning);
    }

    private void givenAggregateDataAccess2() {
        includeClass(MyAggregateDataAccess2.class);
    }

    @Test
    public void correctDataAccessImplementationName() {
        givenValidator();
        givenAggregateDataAccessImplementation();
        whenValidating();
        thenNone(this::dataAccessImplementationNameWarning);
    }

    private void givenAggregateDataAccessImplementation() {
        includeClass(MyAggregateInternalDataAccess.class);
    }

    private boolean dataAccessImplementationNameWarning(ValidationMessage message) {
        return message.type() == ValidationMessageType.WARNING
                && message.location().source().id().contains("/MyAggregateInternalDataAccess")
                && message.message().contains("Data access implementation name does not follow naming convention");
    }

    @Test
    public void warnWrongDataAccessImplementationName() {
        givenValidator();
        givenAggregateDataAccessImplementation2();
        whenValidating();
        thenAtLeast(this::dataAccessImplementationNameWarning);
    }

    private void givenAggregateDataAccessImplementation2() {
        includeClass(MyAggregateInternalDataAccess2.class);
    }
}
