package process;

import java.util.Optional;
import org.junit.Test;
import poussecafe.mymodule.domain.YetAnotherDomainEvent;
import poussecafe.mymodule.domain.myaggregate.MyAggregate;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;
import poussecafe.mymodule.domain.myaggregate.MyAggregateRepository;

import static org.junit.Assert.assertTrue;

/*
 * Verifies that repository message listener behaves as expected.
 */
public class MyAggregateRemovalWithYetAnotherDomainEvent extends MyModuleTest {

    @Test
    public void anotherDomainEventUpdatesAggregate() {
        givenExistingAggregate();
        givenYetAnotherDomainEvent();
        whenEventEmitted();
        thenAggregateRemoved();
    }

    private void givenExistingAggregate() {
        loadDataFile("/existingMyAggregate.json");
    }

    private void givenYetAnotherDomainEvent() {
        event = newDomainEvent(YetAnotherDomainEvent.class);
        event.identifier().value(new MyAggregateId("aggregate-id"));
    }

    private YetAnotherDomainEvent event;

    private void whenEventEmitted() {
        issue(event);
    }

    private void thenAggregateRemoved() {
        Optional<MyAggregate> aggregate = repository.getOptional(event.identifier().value());
        assertTrue(aggregate.isEmpty());
    }

    private MyAggregateRepository repository;
}
