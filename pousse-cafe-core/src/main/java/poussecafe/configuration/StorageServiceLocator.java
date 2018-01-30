package poussecafe.configuration;

import poussecafe.storable.Environment;
import poussecafe.storable.StorableImplementation;
import poussecafe.storage.TransactionRunner;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class StorageServiceLocator {

    public void setEnvironment(Environment environment) {
        checkThat(value(environment).notNull());
        this.environment = environment;
    }

    private Environment environment;

    public TransactionRunner locateTransactionRunner(Class<?> storableClass) {
        StorableImplementation implementation = environment.getStorableImplementation(storableClass);
        return implementation.getStorage().getTransactionRunner();
    }

}
