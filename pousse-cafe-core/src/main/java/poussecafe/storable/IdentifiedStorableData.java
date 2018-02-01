package poussecafe.storable;

public interface IdentifiedStorableData<K> extends StorableData {

    Property<K> key();
}
