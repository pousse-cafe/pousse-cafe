package process;

import org.junit.Test;
import poussecafe.myboundedcontext.domain.ADomainEvent;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregate;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregateKey;

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
        event.key().value(new MyAggregateKey("a-key"));
    }

    private ADomainEvent event;

    private void whenEventEmitted() {
        emitDomainEvent(event);
    }

    private void thenAggregateCreated() {
        MyAggregate aggregate = find(MyAggregate.class, event.key().value());
        assertThat(aggregate, notNullValue());
    }
}
