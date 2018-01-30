package poussecafe.storable;

public abstract class IdentifiedStorable<K, D extends StorableData> extends Storable<D> {

    public abstract K getKey();

    public abstract void setKey(K key);
}
