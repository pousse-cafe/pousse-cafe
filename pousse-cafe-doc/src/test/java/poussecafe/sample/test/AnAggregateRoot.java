package poussecafe.sample.test;

import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

public class AnAggregateRoot extends AggregateRoot<AnAggregateRootId, AnAggregateRoot.Attributes> {

    /**
     * @from_external From
     */
    @MessageListener(processes = Test.class)
    @ProducesEvent(value = Event2.class)
    public void listener(Event1 event1) {
        Event2 event2 = newDomainEvent(Event2.class);
        emitDomainEvent(event2);
    }

    @MessageListener
    public void listener(Event3 event1) {
        // Just a test
    }

    public static interface Attributes extends EntityAttributes<AnAggregateRootId> {

    }
}
