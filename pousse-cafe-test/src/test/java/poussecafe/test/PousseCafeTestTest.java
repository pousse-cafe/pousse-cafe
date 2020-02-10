package poussecafe.test;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.discovery.BundleConfigurer;
import poussecafe.discovery.MessageListener;
import poussecafe.runtime.Runtime.Builder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PousseCafeTestTest extends PousseCafeTest {

    @Override
    protected Builder runtimeBuilder() {
        return super.runtimeBuilder()
                .withBundle(new BundleConfigurer.Builder()
                .moduleBasePackage("poussecafe.test")
                .build()
                .defineAndImplementDefault()
                .build());
    }

    @Test
    public void waitUntilEmptyOrInterruptedReturnsTrueWhenAllMessagesIssued() {
        givenMessages();
        whenEmitted();
        thenAllRecordedAfterWait();
    }

    private void givenMessages() {
        for(int i = 0; i < 1000; ++i) {
            messages.add(new SampleMessage());
        }
    }

    private List<SampleMessage> messages = new ArrayList<>();

    private void whenEmitted() {
        for(SampleMessage message : messages) {
            emitDomainEvent(message);
        }
    }

    @MessageListener
    public void recordMessage(SampleMessage message) {
        recordedMessages.add(message);
    }

    private List<SampleMessage> recordedMessages = new ArrayList<>();

    private void thenAllRecordedAfterWait() {
        waitUntilAllMessageQueuesEmpty();
        assertThat(recordedMessages.size(), is(messages.size()));
    }
}
