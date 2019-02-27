package poussecafe.runtime;

import java.util.Objects;
import poussecafe.environment.EntityImplementation;
import poussecafe.environment.Environment;
import poussecafe.storage.TransactionRunner;

public class TransactionRunnerLocator {

    void setEnvironment(Environment environment) {
        Objects.requireNonNull(environment);
        this.environment = environment;
    }

    private Environment environment;

    public TransactionRunner locateTransactionRunner(Class<?> entityClass) {
        EntityImplementation implementation = environment.entityImplementation(entityClass);
        return implementation.getStorage().getTransactionRunner();
    }

}
