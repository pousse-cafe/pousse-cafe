package poussecafe.storable;

public class SimpleStorable extends ActiveStorable<SimpleStorableKey, SimpleStorable.Data> {

    public static interface Data extends IdentifiedStorableData<SimpleStorableKey> {

    }
}
