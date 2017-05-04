package poussecafe.journal;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.util.KeyTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class JournalEntryKeyTest extends KeyTest<JournalEntryKey> {

    private static final String REFERENCE_MESSAGE_ID = "messageId1";

    private static final String REFERENCE_LISTENER_ID = "listener1";

    @Override
    protected JournalEntryKey referenceKey() {
        return new JournalEntryKey(REFERENCE_MESSAGE_ID, REFERENCE_LISTENER_ID);
    }

    @Override
    protected List<Object> otherKeys() {
        List<Object> keys = new ArrayList<>();
        keys.add(new JournalEntryKey("messageId1", "listener2"));
        keys.add(new JournalEntryKey("messageId2", "listener1"));
        keys.add(new JournalEntryKey("messageId2", "listener2"));
        return keys;
    }

    @Test
    public void gettersWork() {
        givenOnlyOneKey();
        thenExpectedValuesAreReturned();
    }

    private void thenExpectedValuesAreReturned() {
        assertThat(referenceKey.getMessageId(), equalTo(REFERENCE_MESSAGE_ID));
        assertThat(referenceKey.getListenerId(), equalTo(REFERENCE_LISTENER_ID));
    }

}
