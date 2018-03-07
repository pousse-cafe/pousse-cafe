package poussecafe.storable;

public abstract class ActiveStorableFactory<K, A extends ActiveStorable<K, D>, D extends IdentifiedStorableData<K>>
        extends IdentifiedStorableFactory<K, A, D> {

}
