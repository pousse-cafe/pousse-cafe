package poussecafe.data.memory;

import java.lang.reflect.Proxy;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class InMemoryDataImplementationFactory<D> {

    private Class<D> dataClass;

    public InMemoryDataImplementationFactory(Class<D> dataClass) {
        setDataClass(dataClass);
    }

    private void setDataClass(Class<D> dataClass) {
        checkThat(value(dataClass).notNull().because("Class cannot be null"));
        this.dataClass = dataClass;
    }

    @SuppressWarnings("unchecked")
    public D newImplementation() {
        return (D) Proxy.newProxyInstance(dataClass.getClassLoader(), new Class[] { dataClass },
                new InMemoryDataInvocationHandler(dataClass));
    }
}
