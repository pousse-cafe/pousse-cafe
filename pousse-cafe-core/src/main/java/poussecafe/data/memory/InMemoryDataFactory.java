package poussecafe.data.memory;

import poussecafe.storable.StorableData;
import poussecafe.storable.StorableDataFactory;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class InMemoryDataFactory<D extends StorableData<?>> implements StorableDataFactory<D> {

    private Class<D> dataClass;

    public InMemoryDataFactory(Class<D> dataClass) {
        setDataClass(dataClass);
    }

    private void setDataClass(Class<D> dataClass) {
        checkThat(value(dataClass).notNull().because("Data class cannot be null"));
        this.dataClass = dataClass;
    }

    @Override
    public D buildStorableData() {
        return InMemoryDataUtils.newDataImplementation(dataClass);
    }

}
