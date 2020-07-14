package poussecafe.source.testmodel.aggregate3;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

public class Aggregate3 extends AggregateRoot<String, Aggregate3.Attributes> {

    public static interface Attributes extends EntityAttributes<String> {

    }
}
