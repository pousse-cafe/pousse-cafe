package process;

import org.junit.Test;
import poussecafe.mymodule.domain.AnotherDomainEvent;
import poussecafe.mymodule.domain.myaggregate.MyAggregate;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;
import poussecafe.mymodule.domain.myaggregate.MyAggregateRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/*
 * Verifies that aggregate message listener behaves as expected.
 */
public class MyAggregateUpdateWithAnotherDomainEvent extends MyModuleTest {

    @Test
    public void anotherDomainEventUpdatesAggregate() {
        givenExistingAggregate();
        givenAnotherDomainEvent();
        whenEventEmitted();
        thenAggregateUpdated();
    }

    private void givenExistingAggregate() {
        loadDataFile("/existingMyAggregate.json");
    }

    private void givenAnotherDomainEvent() {
        event = newDomainEvent(AnotherDomainEvent.class);
        event.identifier().value(new MyAggregateId("aggregate-id"));
        event.x().value(42);
    }

    private AnotherDomainEvent event;

    private void whenEventEmitted() {
        issue(event);
    }

    private void thenAggregateUpdated() {
        MyAggregate aggregate = repository.getOptional(event.identifier().value()).orElseThrow();
        assertThat(aggregate.attributes().x().value(), is(event.x().value()));
    }

    private MyAggregateRepository repository;
}
