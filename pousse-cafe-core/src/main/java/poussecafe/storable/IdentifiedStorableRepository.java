package poussecafe.storable;

import java.util.List;
import poussecafe.domain.DomainException;

import static java.util.stream.Collectors.toList;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class IdentifiedStorableRepository<A extends IdentifiedStorable<K, D>, K, D extends StorableData>
        extends Primitive {

    @SuppressWarnings("unchecked")
    void setStorableClass(Class<?> storableClass) {
        checkThat(value(storableClass).notNull().because("Storable class cannot be null"));
        this.storableClass = (Class<A>) storableClass;
    }

    private Class<A> storableClass;

    public A find(K key) {
        checkKey(key);
        D data = dataAccess.findData(key);
        if (data == null) {
            return null;
        } else {
            return newStorableWithData(data);
        }
    }

    protected IdentifiedStorableDataAccess<D> dataAccess;

    private void checkKey(K key) {
        checkThat(value(key).notNull().because("Key cannot be null"));
    }

    protected A newStorableWithData(D data) {
        A storable = newPrimitive(
                new PrimitiveSpecification.Builder<A>().withPrimitiveClass(storableClass).withData(false).build());
        storable.setData(data);
        return storable;
    }

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
        dataAccess.addData(data);
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
        dataAccess.updateData(data);
    }

    public void delete(K key) {
        checkKey(key);
        A storable = find(key);
        if (storable != null) {
            deleteData(storable);
        }
    }

    protected void deleteData(A storable) {
        dataAccess.deleteData(storable.getKey());
    }

    protected List<A> newStorablesWithData(List<D> data) {
        return data.stream().map(this::newStorableWithData).collect(toList());
    }

    @SuppressWarnings("unchecked")
    void setDataAccess(Object dataAccess) {
        checkThat(value(dataAccess).notNull().because("Data access cannot be null"));
        this.dataAccess = (IdentifiedStorableDataAccess<D>) dataAccess;
    }

    public IdentifiedStorableDataAccess<D> getDataAccess() {
        return dataAccess;
    }

}
