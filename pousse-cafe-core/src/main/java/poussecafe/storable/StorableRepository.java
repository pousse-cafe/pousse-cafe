package poussecafe.storable;

import java.util.List;
import poussecafe.domain.DomainException;

import static java.util.stream.Collectors.toList;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class StorableRepository<A extends Storable<K, D>, K, D extends StorableData<K>> {

    protected StorableDataAccess<K, D> dataAccess;

    protected StorableDataFactory<D> storableDataFactory;

    public A find(K key) {
        checkKey(key);
        D data = this.dataAccess.findData(key);
        if (data == null) {
            return null;
        } else {
            return newStorableWithData(data);
        }
    }

    private void checkKey(K key) {
        checkThat(value(key).notNull().because("Key cannot be null"));
    }

    protected A newStorableWithData(D data) {
        A storable = newStorable();
        storable.setData(data);
        return storable;
    }

    protected abstract A newStorable();

    public A get(K key) {
        A storable = find(key);
        if (storable == null) {
            throw new DomainException("Storable with key " + key + " not found");
        } else {
            return storable;
        }
    }

    public void add(A storable) {
        checkStorable(storable);
        addData(storable);
    }

    protected void addData(A storable) {
        D data = storable.getData();
        this.dataAccess.addData(data);
    }

    private void checkStorable(A storable) {
        checkThat(value(storable).notNull().because("Storable cannot be null"));
        checkThat(value(storable.getData()).notNull().because("Storable data cannot be null"));
    }

    public void update(A storable) {
        checkStorable(storable);
        updateData(storable);
    }

    protected void updateData(A storable) {
        D data = storable.getData();
        this.dataAccess.updateData(data);
    }

    public void delete(K key) {
        checkKey(key);
        A storable = find(key);
        if (storable != null) {
            deleteData(storable);
        }
    }

    protected void deleteData(A storable) {
        this.dataAccess.deleteData(storable.getKey());
    }

    protected List<A> newStorablesWithData(List<D> data) {
        return data.stream().map(this::newStorableWithData).collect(toList());
    }

    public void setDataAccess(StorableDataAccess<K, D> dataAccess) {
        checkThat(value(dataAccess).notNull().because("Data access cannot be null"));
        this.dataAccess = dataAccess;
    }

    public void setStorableDataFactory(StorableDataFactory<D> storableDataFactory) {
        checkThat(value(storableDataFactory)
                .notNull()
                .because("Storable implementation factory cannot be null"));
        this.storableDataFactory = storableDataFactory;
    }

    public StorableDataAccess<K, D> getDataAccess() {
        return dataAccess;
    }

}
