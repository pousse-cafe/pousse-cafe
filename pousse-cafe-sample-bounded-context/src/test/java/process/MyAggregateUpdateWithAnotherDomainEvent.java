package process;

import org.junit.Test;
import poussecafe.myboundedcontext.domain.AnotherDomainEvent;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregate;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregateKey;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/*
 * Verifies that aggregate message listener behaves as expected.
 */
public class MyAggregateUpdateWithAnotherDomainEvent extends MyBoundedContextTest {

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
        event.key().value(new MyAggregateKey("aggregate-key"));
        event.x().value(42);
    }

    private AnotherDomainEvent event;

    private void whenEventEmitted() {
        emitDomainEvent(event);
    }

    private void thenAggregateUpdated() {
        MyAggregate aggregate = find(MyAggregate.class, event.key().value());
        assertThat(aggregate.attributes().x().value(), is(event.x().value()));
    }
}
