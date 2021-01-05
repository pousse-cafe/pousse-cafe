package poussecafe.source.validation.namesnomodule;

import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

public class MyEntity extends Entity<String, MyEntity.Attributes> {

    public static interface Attributes extends EntityAttributes<String> {

    }
}
