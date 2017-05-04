package poussecafe.storable;

public abstract class ActiveStorableFactory<K, A extends ActiveStorable<K, D>, D extends StorableData<K>>
extends StorableFactory<K, A, D> {

    @Override
    protected A newStorableWithKey(K key) {
        A storable = super.newStorableWithKey(key);
        storable.setMessageCollection(new DefaultMessageCollection());
        return storable;
    }

}
