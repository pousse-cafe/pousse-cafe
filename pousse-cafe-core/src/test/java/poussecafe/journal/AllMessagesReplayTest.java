package poussecafe.journal;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.journal.domain.ConsumptionFailure;
import poussecafe.messaging.Message;

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
        failures.add(failureWithMessage("id1"));
        failures.add(failureWithMessage("id2"));
        failures.add(failureWithMessage("id3"));
        when(consumptionFailureRepository.findAllConsumptionFailures()).thenReturn(failures);
    }

    protected ConsumptionFailure failureWithMessage(String messageId) {
        Message message = messageWithId(messageId);
        ConsumptionFailure entry = failureWithMessage(message);
        return entry;
    }

    private void whenReplayAll() {
        messageReplayer.replayAllFailedConsumptions();
    }

    private void thenFailedReplayed() {
        for (ConsumptionFailure failedEntry : failures) {
            verify(messageSender).sendMessage(failedEntry.getMessage());
        }
    }
}
