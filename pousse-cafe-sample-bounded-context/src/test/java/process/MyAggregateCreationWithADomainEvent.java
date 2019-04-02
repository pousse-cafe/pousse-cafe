package process;

import org.junit.Test;
import poussecafe.myboundedcontext.domain.ADomainEvent;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregate;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregateId;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/*
 * Verifies that factory message listener behaves as expected.
 */
public class MyAggregateCreationWithADomainEvent extends MyBoundedContextTest {

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
        emitDomainEvent(event);
    }

    private void thenAggregateCreated() {
        MyAggregate aggregate = find(MyAggregate.class, event.identifier().value());
        assertThat(aggregate, notNullValue());
    }
}
