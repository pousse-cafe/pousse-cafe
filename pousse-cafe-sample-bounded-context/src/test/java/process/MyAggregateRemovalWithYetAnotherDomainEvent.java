package process;

import org.junit.Test;
import poussecafe.myboundedcontext.domain.YetAnotherDomainEvent;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregate;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregateId;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/*
 * Verifies that repository message listener behaves as expected.
 */
public class MyAggregateRemovalWithYetAnotherDomainEvent extends MyBoundedContextTest {

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
        emitDomainEvent(event);
    }

    private void thenAggregateRemoved() {
        MyAggregate aggregate = find(MyAggregate.class, event.identifier().value());
        assertThat(aggregate, nullValue());
    }
}
