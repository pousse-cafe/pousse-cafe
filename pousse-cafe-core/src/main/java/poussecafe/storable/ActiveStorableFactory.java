package poussecafe.storable;

public abstract class ActiveStorableFactory<K, A extends ActiveStorable<K, D>, D extends ActiveStorableData<K>>
        extends IdentifiedStorableFactory<K, A, D> {

}
