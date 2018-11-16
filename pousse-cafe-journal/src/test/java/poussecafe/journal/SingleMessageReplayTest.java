package poussecafe.journal;

import org.junit.Test;
import poussecafe.journal.domain.ConsumptionFailure;
import poussecafe.journal.domain.ConsumptionFailureKey;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleMessageReplayTest extends MessageReplayTest {

    @Test
    public void failedMessageIsRoutedOnReplay() {
        givenFailedConsumption();
        whenReplayingMessage();
        thenMessageIsRouted();
    }

    private void givenFailedConsumption() {
        key = consumptionFailureKey();
        ConsumptionFailure failure = failureWithKey(key);
        when(consumptionFailureRepository.findConsumptionFailures(key.consumptionId())).thenReturn(asList(failure));
    }

    private ConsumptionFailureKey key;

    private void whenReplayingMessage() {
        messageReplayer.replayMessage(key.consumptionId());
    }

    private void thenMessageIsRouted() {
        verify(messageSender).sendMessage(key.message());
    }

    @Test
    public void successMessageIsNotRoutedOnReplay() {
        givenNoFailedMessage();
        whenReplayingMessage();
        thenNoMessageIsRouted();
    }

    private void givenNoFailedMessage() {
        givenNoFailedConsumption();
    }

    private void givenNoFailedConsumption() {
        key = consumptionFailureKey();
        when(consumptionFailureRepository.findConsumptionFailures(key.consumptionId())).thenReturn(emptyList());
    }
}
