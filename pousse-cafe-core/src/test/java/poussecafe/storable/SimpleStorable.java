package poussecafe.storable;

public class SimpleStorable extends IdentifiedStorable<SimpleStorableKey, SimpleStorable.Data> {

    public static interface Data extends IdentifiedStorableData<SimpleStorableKey> {

    }
}
