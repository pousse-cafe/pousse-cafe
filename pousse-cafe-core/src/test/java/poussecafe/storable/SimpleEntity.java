package poussecafe.storable;

import poussecafe.domain.Entity;
import poussecafe.storable.IdentifiedStorableData;
import poussecafe.util.StringKey;

public class SimpleEntity extends Entity<StringKey, SimpleEntity.Data> {

    public static interface Data extends IdentifiedStorableData<StringKey> {

    }
}
