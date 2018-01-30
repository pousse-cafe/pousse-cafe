package poussecafe.storable;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public abstract class IdentifiedStorableFactory<K, A extends IdentifiedStorable<K, D>, D extends StorableData> extends Primitive {

    @SuppressWarnings("unchecked")
    void setStorableClass(Class<?> storableClass) {
        checkThat(value(storableClass).notNull().because("Storable class cannot be null"));
        this.storableClass = (Class<A>) storableClass;
    }

    private Class<A> storableClass;

    protected A newStorableWithKey(K key) {
        checkThat(value(key).notNull().because("Key cannot be null"));
        A storable = newPrimitive(storableClass);
        storable.setKey(key);
        return storable;
    }
}
