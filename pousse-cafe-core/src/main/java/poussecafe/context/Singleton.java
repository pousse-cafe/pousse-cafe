package poussecafe.context;

import java.util.function.Supplier;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class Singleton<T> implements Supplier<T> {

    private T singleton;

    private Supplier<T> factory;

    public Singleton(Supplier<T> factory) {
        setFactory(factory);
    }

    private void setFactory(Supplier<T> supplier) {
        checkThat(value(supplier).notNull().because("Instance supplier cannot be null"));
        this.factory = supplier;
    }

    @Override
    public T get() {
        if(singleton == null) {
            singleton = factory.get();
        }
        return singleton;
    }
}
