package poussecafe.journal;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import poussecafe.journal.domain.ConsumptionFailure;
import poussecafe.journal.domain.ConsumptionFailureRepository;
import poussecafe.journal.domain.MessageReplayer;
import poussecafe.messaging.Message;
import poussecafe.messaging.MessageSender;

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

    protected ConsumptionFailure failureWithMessage(Message message) {
        ConsumptionFailure failure = mock(ConsumptionFailure.class);
        when(failure.getMessage()).thenReturn(message);
        return failure;
    }

    protected void thenMessageIsRouted(Message message) {
        verify(messageSender).sendMessage(message);
    }

    protected void thenMessageIsNotRouted(Message message) {
        verify(messageSender, never()).sendMessage(message);
    }

    protected Message messageWithId(String id) {
        Message message = mock(Message.class);
        when(message.getId()).thenReturn(id);
        return message;
    }
}
