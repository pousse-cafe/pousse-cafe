package poussecafe.sample.test;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = AnAggregateRootFactory.class,
    repository = AnAggregateRootRepository.class
)
public class AnAggregateRoot extends AggregateRoot<AnAggregateRootId, AnAggregateRoot.Attributes> {

    @MessageListener(processes = Test.class, consumesFromExternal = "From")
    @ProducesEvent(value = Event2.class)
    public void listener(Event1 event1) {
        Event2 event2 = newDomainEvent(Event2.class);
        issue(event2);
    }

    @MessageListener
    public void listener(Event3 event1) {
        // Just a test
    }

    public static interface Attributes extends EntityAttributes<AnAggregateRootId> {

    }
}
