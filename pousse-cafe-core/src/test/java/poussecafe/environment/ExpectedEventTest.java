package poussecafe.environment;

import org.junit.Test;
import poussecafe.domain.DomainEvent;
import poussecafe.runtime.TestDomainEvent;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExpectedEventTest {

    @Test
    public void expectedMatches() {
        givenExpectedEvent(SampleEvent.class);
        givenProducedEvent(new SampleEventData());
        whenMatching();
        thenMatches(true);
    }

    private void givenExpectedEvent(Class<? extends DomainEvent> expectedDomainEventClass) {
        expectedEvent = new ExpectedEvent.Builder()
                .producedEventClass(expectedDomainEventClass)
                .build();
    }

    private ExpectedEvent expectedEvent;

    private void givenProducedEvent(DomainEvent domainEvent) {
        producedEvent = domainEvent;
    }

    private DomainEvent producedEvent;

    private void whenMatching() {
        matches = expectedEvent.matches(producedEvent);
    }

    private boolean matches;

    private void thenMatches(boolean expectedValue) {
        assertThat(matches, is(expectedValue));
    }

    @Test
    public void expectedDoesNotMatchOther() {
        givenExpectedEvent(TestDomainEvent.class);
        givenProducedEvent(new SampleEventData());
        whenMatching();
        thenMatches(false);
    }
}
