package poussecafe.storable;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class StorableFactory<K, A extends Storable<K, D>, D extends StorableData<K>> {

    protected StorableDataFactory<D> storableDataFactory;

    public void setStorableDataFactory(StorableDataFactory<D> storableDataFactory) {
        checkThat(value(storableDataFactory).notNull()
                .because("Aggregate implementation factory cannot be null"));
        this.storableDataFactory = storableDataFactory;
    }

    protected A newStorableWithKey(K key) {
        checkThat(value(key).notNull().because("Key cannot be null"));
        A storable = newStorable();
        storable.setData(storableDataFactory.buildStorableData());
        storable.getData().setKey(key);
        return storable;
    }

    protected abstract A newStorable();

}
