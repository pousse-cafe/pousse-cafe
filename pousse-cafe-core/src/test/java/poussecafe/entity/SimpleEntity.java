package poussecafe.entity;

import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;
import poussecafe.util.StringKey;

public class SimpleEntity extends Entity<StringKey, SimpleEntity.Attributes> {

    @Override
    public boolean equals(Object obj) {
        SimpleEntity other = (SimpleEntity) obj;
        return attributes().key().value().equals(other.attributes().key().value());
    }

    public static interface Attributes extends EntityAttributes<StringKey> {

    }
}
