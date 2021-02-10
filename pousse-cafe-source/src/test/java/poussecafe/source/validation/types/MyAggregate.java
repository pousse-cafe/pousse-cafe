package poussecafe.source.validation.types;

import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate
public class MyAggregate {

    public static class Root extends AggregateRoot<String, Root.Attributes> {

        public static interface Attributes extends EntityAttributes<String> {

        }
    }

    public static class Factory extends AggregateFactory<String, Root, Root.Attributes> {

    }

    public static class Repository extends AggregateRepository<String, Root, Root.Attributes> {

    }
}
