package poussecafe.storable;

public interface EntityMapProperty<K, E extends IdentifiedStorable<K, ?>> {

    MapProperty<K, E> inContextOf(Primitive primitive);

    E newInContextOf(Primitive primitive);
}
