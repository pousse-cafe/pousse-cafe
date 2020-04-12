package poussecafe.testmodule;

import org.junit.Test;
import poussecafe.domain.MessageCollectionValidator;
import poussecafe.messaging.Message;
import poussecafe.storage.DefaultMessageCollection;
import poussecafe.storage.MessageCollection;

import static org.junit.Assert.assertTrue;

public class MessageCollectionValidatorTest {

    @Test(expected = IllegalArgumentException.class)
    public void messageEmittedTwiceThrows() {
        givenMessageEmittedTwice();
        whenValidatingMessageCollection();
    }

    private void givenMessageEmittedTwice() {
        messageCollection = new DefaultMessageCollection();
        Message message = new SimpleMessage();
        messageCollection.addMessage(message);
        messageCollection.addMessage(message);
    }

    private MessageCollection messageCollection;

    private void whenValidatingMessageCollection() {
        messageCollectionValidator.validate(messageCollection);
    }

    private MessageCollectionValidator messageCollectionValidator = new MessageCollectionValidator();

    @Test
    public void distinctMessagesDoesNotThrow() {
        givenDistinctMessages();
        whenValidatingMessageCollection();
        thenNoExceptionThrown();
    }

    private void givenDistinctMessages() {
        messageCollection = new DefaultMessageCollection();
        messageCollection.addMessage(new SimpleMessage());
        messageCollection.addMessage(new SimpleMessage());
        messageCollection.addMessage(new SimpleMessage());
    }

    private void thenNoExceptionThrown() {
        assertTrue(true);
    }
}
