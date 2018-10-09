package poussecafe.entity;

import poussecafe.domain.Entity;
import poussecafe.domain.EntityData;
import poussecafe.util.StringKey;

public class SimpleEntity extends Entity<StringKey, SimpleEntity.Data> {

    @Override
    public boolean equals(Object obj) {
        SimpleEntity other = (SimpleEntity) obj;
        return getKey().equals(other.getKey());
    }

    public static interface Data extends EntityData<StringKey> {

    }
}
