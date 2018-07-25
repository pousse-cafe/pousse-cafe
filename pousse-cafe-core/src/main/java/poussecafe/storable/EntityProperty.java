package poussecafe.storable;

public interface EntityProperty<E extends IdentifiedStorable<?, ?>> {

    Property<E> with(Primitive primitive);
}
