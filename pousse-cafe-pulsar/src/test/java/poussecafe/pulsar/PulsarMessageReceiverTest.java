package poussecafe.pulsar;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClientException;
import org.junit.Test;
import org.mockito.stubbing.Answer;
import poussecafe.processing.MessageBroker;
import poussecafe.processing.ReceivedMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PulsarMessageReceiverTest {

    @Test
    public void givenMessageHandlingExceptionThenMessageAcked() throws PulsarClientException {
        givenExceptionThrowingMessageBroker();
        givenMessageReceiver();
        givenMessage();
        whenConsumingMessage();
        thenMessageAcked();
    }

    private void givenExceptionThrowingMessageBroker() {
        messageBroker = mock(MessageBroker.class);
        doThrow(IllegalArgumentException.class).when(messageBroker).dispatch(any(ReceivedMessage.class));
    }

    private MessageBroker messageBroker;

    @SuppressWarnings("unchecked")
    private void givenMessageReceiver() throws PulsarClientException {
        consumer = mock(Consumer.class);
        when(consumer.receive()).thenAnswer(receive());

        ConsumerFactory consumerFactory = mock(ConsumerFactory.class);
        when(consumerFactory.buildConsumer()).thenReturn(consumer);

        receiver = new PulsarMessageReceiver.Builder()
                .consumerFactory(consumerFactory)
                .messageBroker(messageBroker)
                .build();
        receiver.startReceiving();
    }

    private Consumer<String> consumer;

    private Answer<Message<String>> receive() {
        return invocation -> {
            if(invocationCount == 0) {
                ++invocationCount;
                return messages.take();
            } else {
                throw new PulsarClientException("Auto-closing");
            }
        };
    }

    private int invocationCount;

    private BlockingQueue<Message<String>> messages = new LinkedBlockingDeque<>();

    private PulsarMessageReceiver receiver;

    @SuppressWarnings("unchecked")
    private void givenMessage() {
        message = mock(Message.class);
        when(message.getValue()).thenReturn("{}");
    }

    private Message<String> message;

    private void whenConsumingMessage() {
        try {
            messages.put(message);
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
    }

    private void thenMessageAcked() {
        receiver.join();
        try {
            verify(consumer).acknowledge(message);
        } catch (PulsarClientException e) {
            throw new IllegalStateException();
        }
    }
}
