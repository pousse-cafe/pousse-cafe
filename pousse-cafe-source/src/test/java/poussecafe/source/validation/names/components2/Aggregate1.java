package poussecafe.source.validation.names.components2;

import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate
public class Aggregate1 {

    public static class Root extends AggregateRoot<String, Root.Attributes> {

        public static interface Attributes extends EntityAttributes<String> {

        }
    }
}
