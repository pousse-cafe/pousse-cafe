package poussecafe.source.models.p214;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateFactory;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate
public class A1 {

    public static class Root extends AggregateRoot<A2Id, Root.Attributes> {

        public static interface Attributes extends EntityAttributes<A2Id> {

        }
    }

    public static class Factory extends AggregateFactory<A2Id, Root, Root.Attributes> {

        @MessageListener(processes = P214.class)
        @ProducesEvent(E2.class)
        public Root newA1(E1 event) {
            return null;
        }
    }

    public static class Repository extends AggregateRepository<A2Id, Root, Root.Attributes> {

    }
}
