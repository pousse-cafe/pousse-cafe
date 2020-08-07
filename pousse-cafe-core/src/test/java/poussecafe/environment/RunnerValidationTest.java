package poussecafe.environment;

import org.junit.Test;
import poussecafe.discovery.AggregateRootWithSingleListener;
import poussecafe.testmodule.SimpleAggregate;
import poussecafe.testmodule.TestDomainEvent;
import poussecafe.testmodule.TestDomainEvent3;
import poussecafe.util.ClassHierarchyTypeArgumentsValidator;

public abstract class RunnerValidationTest {

    @Test
    public void validRunner() {
        givenValidatorWithValidRunner();
        whenValidating();
    }

    private void givenValidatorWithValidRunner() {
        runnerValidator = new ClassHierarchyTypeArgumentsValidator.Builder()
                .listenerId("listenerId")
                .runner(runnerClass())
                .expectedTypeArgument(SimpleAggregate.class)
                .expectedTypeArgument(TestDomainEvent3.class)
                .build();
    }

    @SuppressWarnings("rawtypes")
    protected abstract Class runnerClass();

    private ClassHierarchyTypeArgumentsValidator runnerValidator;

    private void whenValidating() {
        runnerValidator.validOrThrow();
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidRunnerWithWrongAggregateRoot() {
        givenValidatorWithInvalidRunnerWrongAggregateRoot();
        whenValidating();
    }

    private void givenValidatorWithInvalidRunnerWrongAggregateRoot() {
        runnerValidator = new ClassHierarchyTypeArgumentsValidator.Builder()
                .listenerId("listenerId")
                .runner(SingleLevelTypesRunner.class)
                .expectedTypeArgument(AggregateRootWithSingleListener.class)
                .expectedTypeArgument(TestDomainEvent3.class)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidRunnerWithWrongMessage() {
        givenValidatorWithInvalidRunnerWrongMessage();
        whenValidating();
    }

    private void givenValidatorWithInvalidRunnerWrongMessage() {
        runnerValidator = new ClassHierarchyTypeArgumentsValidator.Builder()
                .listenerId("listenerId")
                .runner(SingleLevelTypesRunner.class)
                .expectedTypeArgument(SimpleAggregate.class)
                .expectedTypeArgument(TestDomainEvent.class)
                .build();
    }
}
