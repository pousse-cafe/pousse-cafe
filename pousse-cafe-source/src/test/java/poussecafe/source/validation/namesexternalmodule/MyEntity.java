package poussecafe.source.validation.namesexternalmodule;

import poussecafe.domain.Entity;
import poussecafe.domain.EntityAttributes;

public class MyEntity extends Entity<String, MyEntity.Attributes> {

    public static interface Attributes extends EntityAttributes<String> {

    }
}
