package poussecafe.domain;

import java.util.List;
import org.junit.Test;
import poussecafe.environment.ExpectedEvent;
import poussecafe.environment.SampleEvent;
import poussecafe.environment.SampleEventData;
import poussecafe.environment.SampleEventData2;
import poussecafe.messaging.Message;
import poussecafe.storage.DefaultMessageCollection;
import poussecafe.storage.MessageCollection;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MessageCollectionValidatorTest {

    @Test
    public void expectedEventsProducedSucceeds() {
        givenExpectedEvents();
        givenIssuedEvents(expectedEvents());
        whenConsume();
        thenReportSuccess();
    }

    private void givenExpectedEvents() {
        var expectedEvent = new ExpectedEvent.Builder()
                .producedEventClass(SampleEvent.class)
                .required(true)
                .build();
        validator.addExpectedEvent(expectedEvent);
    }

    private MessageCollectionValidator validator = new MessageCollectionValidator();

    private void givenIssuedEvents(List<Message> issuedEvents) {
        issuedEvents.forEach(collection::addMessage);
    }

    private MessageCollection collection = new DefaultMessageCollection();

    private List<Message> expectedEvents() {
        return asList(new SampleEventData());
    }

    private void whenConsume() {
        errors = validator.validate(collection);
    }

    private List<MessageCollectionValidator.Error> errors;

    private void thenReportSuccess() {
        assertTrue(errors.isEmpty());
    }

    @Test
    public void missingEventsFails() {
        givenExpectedEvents();
        givenIssuedEvents(emptyList());
        whenConsume();
        thenReportFailure();
    }

    private void thenReportFailure() {
        assertFalse(errors.isEmpty());
    }

    @Test
    public void unexpectedEventFails() {
        givenExpectedEvents();
        givenIssuedEvents(unexpectedEvents());
        whenConsume();
        thenReportFailure();
    }

    private List<Message> unexpectedEvents() {
        return asList(new SampleEventData2());
    }
}
