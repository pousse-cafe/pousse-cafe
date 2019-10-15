package poussecafe.sample.test;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

public class AnotherAggregateRoot extends AggregateRoot<AnotherAggregateRootId, AnotherAggregateRoot.Attributes> {

    /**
     * @process Test
     * @event Event3
     * @to_external To
     */
    @MessageListener
    public void listener(Event2 event2) {
        Event3 event3 = newDomainEvent(Event3.class);
        emitDomainEvent(event3);
    }

    public static interface Attributes extends EntityAttributes<AnotherAggregateRootId> {

    }
}
