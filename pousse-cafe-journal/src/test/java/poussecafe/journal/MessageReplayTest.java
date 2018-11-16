package poussecafe.journal;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import poussecafe.journal.domain.ConsumptionFailure;
import poussecafe.journal.domain.ConsumptionFailureKey;
import poussecafe.journal.domain.ConsumptionFailureRepository;
import poussecafe.journal.domain.MessageReplayer;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageSender;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class MessageReplayTest {

    @Mock
    protected ConsumptionFailureRepository consumptionFailureRepository;

    @Mock
    protected MessageSender messageSender;

    @InjectMocks
    protected MessageReplayer messageReplayer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    protected ConsumptionFailureKey consumptionFailureKey() {
        return consumptionFailureKey("an-id");
    }

    protected ConsumptionFailureKey consumptionFailureKey(String consumptionId) {
        ConsumptionFailureKey key = mock(ConsumptionFailureKey.class);
        when(key.consumptionId()).thenReturn(consumptionId);
        Message message = mock(Message.class);
        when(key.message()).thenReturn(message);
        return key;
    }

    protected ConsumptionFailure failureWithKey(ConsumptionFailureKey key) {
        ConsumptionFailure failure = mock(ConsumptionFailure.class);
        when(failure.getKey()).thenReturn(key);
        return failure;
    }

    protected void thenMessageIsRouted(Message message) {
        verify(messageSender).sendMessage(message);
    }

    protected void thenMessageIsNotRouted(Message message) {
        verify(messageSender, never()).sendMessage(message);
    }

    protected void thenNoMessageIsRouted() {
        verify(messageSender, never()).sendMessage(any());
    }
}
