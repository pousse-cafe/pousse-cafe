package poussecafe.storable;

public interface StorableData<K> {

    void setKey(K key);

    K getKey();
}