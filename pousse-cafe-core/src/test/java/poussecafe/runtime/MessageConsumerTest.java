package poussecafe.runtime;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.mockito.Mockito;
import poussecafe.environment.Environment;
import poussecafe.environment.MessageListener;
import poussecafe.messaging.MessageSender;
import poussecafe.support.model.SuccessfulConsumption;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageConsumerTest {

    @Test
    public void noSuccessfulConsumptionInfiniteLoop() {
        givenMessageConsumer();
        givenSuccessfulConsumptionMessage();
        whenConsumingMessage();
        thenNoMessageSent();
    }

    private void givenMessageConsumer() {
        mockEnvironment();
        mockMessageSenderLocator();
        messageConsumer = new MessageConsumer.Builder()
                .environment(environment)
                .messageSenderLocator(messageSenderLocator)
                .build();
    }

    private void mockEnvironment() {
        environment = mock(Environment.class);
        listeners = new HashSet<>();
        listeners.add(mockListener());
        when(environment.messageListenersOf(Mockito.any())).thenReturn(listeners);
    }

    private Environment environment;

    private Set<MessageListener> listeners;

    private MessageListener mockListener() {
        MessageListener listener = mock(MessageListener.class);
        when(listener.id()).thenReturn("listenerId");
        when(listener.consumer()).thenReturn(message -> {});
        return listener;
    }

    private void mockMessageSenderLocator() {
        messageSenderLocator = mock(MessageSenderLocator.class);
        messageSender = mock(MessageSender.class);
        when(messageSenderLocator.locate(Mockito.any())).thenReturn(messageSender);
    }

    private MessageSenderLocator messageSenderLocator;

    private MessageSender messageSender;

    private MessageConsumer messageConsumer;

    private void givenSuccessfulConsumptionMessage() {
        SuccessfulConsumption original = mock(SuccessfulConsumption.class);
        message = new OriginalAndMarshaledMessage.Builder()
                .marshaled("marshaled")
                .original(original)
                .build();
    }

    private OriginalAndMarshaledMessage message;

    private void whenConsumingMessage() {
        messageConsumer.consumeMessage(message);
    }

    private void thenNoMessageSent() {
        verify(messageSender, never()).sendMessage(Mockito.any());
    }
}
