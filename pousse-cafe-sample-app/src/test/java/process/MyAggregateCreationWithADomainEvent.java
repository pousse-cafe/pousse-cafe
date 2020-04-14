package process;

import java.util.Optional;
import org.junit.Test;
import poussecafe.mymodule.domain.ADomainEvent;
import poussecafe.mymodule.domain.myaggregate.MyAggregate;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;
import poussecafe.mymodule.domain.myaggregate.MyAggregateRepository;

import static org.junit.Assert.assertTrue;

/*
 * Verifies that factory message listener behaves as expected.
 */
public class MyAggregateCreationWithADomainEvent extends MyModuleTest {

    @Test
    public void aDomainEventCreatesAggregate() {
        givenADomainEvent();
        whenEventEmitted();
        thenAggregateCreated();
    }

    private void givenADomainEvent() {
        event = newDomainEvent(ADomainEvent.class);
        event.identifier().value(new MyAggregateId("a-id"));
    }

    private ADomainEvent event;

    private void whenEventEmitted() {
        issue(event);
    }

    private void thenAggregateCreated() {
        Optional<MyAggregate> aggregate = repository.getOptional(event.identifier().value());
        assertTrue(aggregate.isPresent());
    }

    private MyAggregateRepository repository;
}
