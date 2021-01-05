package poussecafe.source.validation.namesnomodule;

import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate
public class MyAggregate {

    public static class Root extends AggregateRoot<String, Root.Attributes> {

        public static interface Attributes extends EntityAttributes<String> {

        }
    }
}
