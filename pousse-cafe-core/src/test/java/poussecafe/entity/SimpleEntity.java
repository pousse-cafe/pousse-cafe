package poussecafe.entity;

import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;
import poussecafe.util.StringId;

public class SimpleEntity extends Entity<StringId, SimpleEntity.Attributes> {

    @Override
    public boolean equals(Object obj) {
        SimpleEntity other = (SimpleEntity) obj;
        return attributes().identifier().value().equals(other.attributes().identifier().value());
    }

    public static interface Attributes extends EntityAttributes<StringId> {

    }
}
