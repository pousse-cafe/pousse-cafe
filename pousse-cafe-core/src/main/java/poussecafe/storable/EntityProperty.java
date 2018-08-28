package poussecafe.storable;

public interface EntityProperty<E extends IdentifiedStorable<?, ?>> {

    Property<E> inContextOf(Primitive primitive);

    E newInContextOf(Primitive primitive);
}
