package poussecafe.storable;

@FunctionalInterface
public interface StorableDataFactory<D extends StorableData<?>> {

    D buildStorableData();
}
