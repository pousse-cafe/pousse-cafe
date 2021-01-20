package poussecafe.testmodule;

import java.util.List;
import org.junit.Test;
import poussecafe.domain.MessageCollectionValidator;
import poussecafe.domain.MessageCollectionValidator.Error.ErrorType;
import poussecafe.environment.ExpectedEvent;
import poussecafe.messaging.Message;
import poussecafe.storage.DefaultMessageCollection;
import poussecafe.storage.MessageCollection;

import static org.junit.Assert.assertTrue;

public class MessageCollectionValidatorTest {

    @Test
    public void messageIssuedTwiceError() {
        givenValidator();
        givenMessageEmittedTwice();
        whenValidatingMessageCollection();
        thenErrorDetected();
    }

    private void givenValidator() {
        messageCollectionValidator.addExpectedEvent(new ExpectedEvent.Builder()
                .required(false)
                .producedEventClass(SimpleMessage.class)
                .build());
    }

    private MessageCollectionValidator messageCollectionValidator = new MessageCollectionValidator();

    private void givenMessageEmittedTwice() {
        messageCollection = new DefaultMessageCollection();
        Message message = new SimpleMessage();
        messageCollection.addMessage(message);
        messageCollection.addMessage(message);
    }

    private MessageCollection messageCollection;

    private void whenValidatingMessageCollection() {
        errors = messageCollectionValidator.validate(messageCollection);
    }

    private List<MessageCollectionValidator.Error> errors;

    private void thenErrorDetected() {
        assertTrue(errors.stream().filter(error -> error.type() == ErrorType.DUPLICATE).findFirst().isPresent());
    }

    @Test
    public void distinctMessagesNoError() {
        givenValidator();
        givenDistinctMessages();
        whenValidatingMessageCollection();
        thenNoError();
    }

    private void givenDistinctMessages() {
        messageCollection = new DefaultMessageCollection();
        messageCollection.addMessage(new SimpleMessage());
        messageCollection.addMessage(new SimpleMessage());
        messageCollection.addMessage(new SimpleMessage());
    }

    private void thenNoError() {
        assertTrue(errors.isEmpty());
    }
}
