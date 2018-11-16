package poussecafe.journal;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.journal.domain.ConsumptionFailure;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AllMessagesReplayTest extends MessageReplayTest {

    private List<ConsumptionFailure> failures;

    @Test
    public void allFailedReplayedRouted() {
        givenFailedMessages();
        whenReplayAll();
        thenFailedReplayed();
    }

    private void givenFailedMessages() {
        failures = new ArrayList<>();
        failures.add(failureWithKey(consumptionFailureKey("id1")));
        failures.add(failureWithKey(consumptionFailureKey("id2")));
        failures.add(failureWithKey(consumptionFailureKey("id3")));
        when(consumptionFailureRepository.findAllConsumptionFailures()).thenReturn(failures);
    }

    private void whenReplayAll() {
        messageReplayer.replayAllFailedConsumptions();
    }

    private void thenFailedReplayed() {
        for (ConsumptionFailure failedEntry : failures) {
            verify(messageSender).sendMessage(failedEntry.getKey().message());
        }
    }
}
