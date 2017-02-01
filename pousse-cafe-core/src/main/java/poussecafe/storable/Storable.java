package poussecafe.storable;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class Storable<K, D extends StorableData<K>> {

    private D data;

    protected void setData(D data) {
        checkThat(value(data).notNull().because("Data cannot be null"));
        this.data = data;
    }

    protected D getData() {
        return data;
    }

    public K getKey() {
        return data.getKey();
    }
}
