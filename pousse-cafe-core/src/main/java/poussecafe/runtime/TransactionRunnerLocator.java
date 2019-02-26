package poussecafe.runtime;

import poussecafe.environment.EntityImplementation;
import poussecafe.environment.Environment;
import poussecafe.storage.TransactionRunner;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class TransactionRunnerLocator {

    public void setEnvironment(Environment environment) {
        checkThat(value(environment).notNull());
        this.environment = environment;
    }

    private Environment environment;

    public TransactionRunner locateTransactionRunner(Class<?> entityClass) {
        EntityImplementation implementation = environment.entityImplementation(entityClass);
        return implementation.getStorage().getTransactionRunner();
    }

}
