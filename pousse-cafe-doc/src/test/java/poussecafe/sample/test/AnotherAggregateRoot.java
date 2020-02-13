package poussecafe.sample.test;

import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.discovery.ProducesEvent;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
    factory = AnotherAggregateRootFactory.class,
    repository = AnotherAggregateRootRepository.class,
    module = Sample.class
)
public class AnotherAggregateRoot extends AggregateRoot<AnotherAggregateRootId, AnotherAggregateRoot.Attributes> {

    @MessageListener(processes = Test.class)
    @ProducesEvent(value = Event3.class, consumedByExternal = "To", required = false)
    public void listener(Event2 event2) {
        Event3 event3 = newDomainEvent(Event3.class);
        issue(event3);
    }

    public static interface Attributes extends EntityAttributes<AnotherAggregateRootId> {

    }
}
