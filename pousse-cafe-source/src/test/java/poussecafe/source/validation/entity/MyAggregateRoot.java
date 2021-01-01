package poussecafe.source.validation.entity;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

public class MyAggregateRoot extends AggregateRoot<String, MyAggregateRoot.Attributes> {

    public static interface Attributes extends EntityAttributes<String> {

    }
}
