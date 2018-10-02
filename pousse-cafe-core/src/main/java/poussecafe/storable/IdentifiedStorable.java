package poussecafe.storable;

import static poussecafe.check.Checks.checkThatValue;

public abstract class IdentifiedStorable<K, D extends IdentifiedStorableData<K>> extends Storable<D> {

    public K getKey() {
        return getData().key().get();
    }

    public void setKey(K key) {
        checkThatValue(key).notNull();
        getData().key().set(key);
    }

    public void parent(Primitive parent) {
        checkThatValue(parent).notNull();
        this.parent = parent;
    }

    private Primitive parent;

    public Primitive parent() {
        return parent;
    }
}
