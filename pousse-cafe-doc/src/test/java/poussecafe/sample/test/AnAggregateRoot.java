package poussecafe.sample.test;

import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

public class AnAggregateRoot extends AggregateRoot<AnAggregateRootId, AnAggregateRoot.Attributes> {

    /**
     * @process Test
     * @from_external From
     */
    @MessageListener
    @ProducesEvent(value = Event2.class)
    public void listener(Event1 event1) {
        Event2 event2 = newDomainEvent(Event2.class);
        emitDomainEvent(event2);
    }

    public static interface Attributes extends EntityAttributes<AnAggregateRootId> {

    }
}
