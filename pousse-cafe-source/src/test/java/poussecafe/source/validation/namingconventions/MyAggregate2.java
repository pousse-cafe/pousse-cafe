package poussecafe.source.validation.namingconventions;

import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate
public class MyAggregate2 {

    public static class Root2 extends AggregateRoot<String, Root2.Attributes> {

        public static interface Attributes extends EntityAttributes<String> {

        }
    }

    public static class Factory2 extends AggregateFactory<String, Root2, Root2.Attributes> {

    }

    public static class Repository2 extends AggregateRepository<String, Root2, Root2.Attributes> {

    }
}
