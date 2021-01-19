package poussecafe.sample.test_deprecated;

import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

/**
 * <p>AnAggregate description.</p>
 */
public class AnAggregateRoot extends AggregateRoot<AnAggregateRootId, AnAggregateRoot.Attributes> {

    /**
     * listener(Event1) description.
     */
    @MessageListener(processes = Test.class, consumesFromExternal = "From")
    @ProducesEvent(value = Event2.class)
    public void listener(Event1 event1) {
        Event2 event2 = newDomainEvent(Event2.class);
        issue(event2);
    }

    /**
     * listener(Event3) description.
     */
    @MessageListener
    public void listener(Event3 event1) {
        // Just a test
    }

    public static interface Attributes extends EntityAttributes<AnAggregateRootId> {

    }
}
