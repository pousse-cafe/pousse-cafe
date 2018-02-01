package poussecafe.storable;

public abstract class IdentifiedStorable<K, D extends IdentifiedStorableData<K>> extends Storable<D> {

    public K getKey() {
        return getData().key().get();
    }

    public void setKey(K key) {
        getData().key().set(key);
    }
}
