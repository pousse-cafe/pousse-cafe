package poussecafe.source.validation.listener;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate
public class MyAggregate {

    public static class Factory extends AggregateFactory<String, Root, Root.Attributes> {

        @MessageListener
        public Root creator(Message1 message) {
            return null;
        }
    }

    public static class Root extends AggregateRoot<String, Root.Attributes> {

        @MessageListener(runner = UpdatorRunner.class)
        public void updator(Message2 message) {

        }

        public static interface Attributes extends EntityAttributes<String> {

        }
    }

    public static class Repository extends AggregateRepository<String, Root, Root.Attributes> {

        @MessageListener
        public void remover(Message3 message) {

        }
    }
}
