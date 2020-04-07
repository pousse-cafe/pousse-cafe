package poussecafe.processing;

import org.junit.Test;
import poussecafe.messaging.Message;
import poussecafe.runtime.OriginalAndMarshaledMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageProcessorTest {

    @Test
    public void messageProcessorMakesGroupConsumeMessage() {
        givenMessageProcessor();
        givenMessageListenersGroup();
        whenProcessingMessage();
        thenMessageForwardedToListenersConsumers();
    }

    private void givenMessageProcessor() {
        messageProcessor = new MessageProcessor.Builder()
                .id("id")
                .messageConsumptionConfiguration(new MessageConsumptionConfiguration.Builder()
                        .backOffCeiling(10)
                        .backOffSlotTime(1.0)
                        .maxConsumptionRetries(10)
                        .build())
                .build();
    }

    private void givenMessageListenersGroup() {
        group = mock(MessageListenersGroup.class);
        OriginalAndMarshaledMessage originalAndMarshaled = mock(OriginalAndMarshaledMessage.class);
        Message original = mock(Message.class);
        when(originalAndMarshaled.original()).thenReturn(original);
        when(group.message()).thenReturn(originalAndMarshaled);
    }

    private MessageListenersGroup group;

    private MessageProcessor messageProcessor;

    private void whenProcessingMessage() {
        messageProcessor.processMessage(group);
    }

    private void thenMessageForwardedToListenersConsumers() {
        verify(group).consumeMessageOrRetry(any());
    }
}
