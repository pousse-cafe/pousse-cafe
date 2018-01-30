package poussecafe.journal;

import org.junit.Test;
import poussecafe.messaging.Message;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleMessageReplayTest extends MessageReplayTest {

    private Message message;

    @Test
    public void failedMessageIsRoutedOnReplay() {
        givenFailedMessage();
        whenReplayingMessage();
        thenMessageIsRouted();
    }

    private void givenFailedMessage() {
        givenMessage();
        givenFailedConsumption();
    }

    private void givenMessage() {
        message = messageWithId("messageId");
    }

    private void givenFailedConsumption() {
        ConsumptionFailure failure = failureWithMessage(message);
        when(consumptionFailureRepository.findConsumptionFailures(message.getId())).thenReturn(asList(failure));
    }

    private void whenReplayingMessage() {
        messageReplayer.replayMessage(message.getId());
    }

    private void thenMessageIsRouted() {
        verify(messageSender).sendMessage(message);
    }

    @Test
    public void successMessageIsNotRoutedOnReplay() {
        givenNoFailedMessage();
        whenReplayingMessage();
        thenMessageIsNotRouted();
    }

    private void givenNoFailedMessage() {
        givenMessage();
        givenNoFailedConsumption();
    }

    private void givenNoFailedConsumption() {
        when(consumptionFailureRepository.findConsumptionFailures(message.getId())).thenReturn(emptyList());
    }

    private void thenMessageIsNotRouted() {
        thenMessageIsNotRouted(message);
    }
}
