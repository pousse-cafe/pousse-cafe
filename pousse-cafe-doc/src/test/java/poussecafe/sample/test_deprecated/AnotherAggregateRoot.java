package poussecafe.sample.test_deprecated;

import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

/**
 * <p>AnotherAggregate description.</p>
 */
public class AnotherAggregateRoot extends AggregateRoot<AnotherAggregateRootId, AnotherAggregateRoot.Attributes> {

    /**
     * listener(Event2) description.
     */
    @MessageListener(processes = Test.class)
    @ProducesEvent(value = Event3.class, consumedByExternal = "To")
    public void listener(Event2 event2) {
        Event3 event3 = newDomainEvent(Event3.class);
        issue(event3);
    }

    public static interface Attributes extends EntityAttributes<AnotherAggregateRootId> {

    }
}
