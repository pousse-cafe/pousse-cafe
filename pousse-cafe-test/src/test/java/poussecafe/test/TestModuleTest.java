package poussecafe.test;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import poussecafe.discovery.BundleConfigurer;
import poussecafe.runtime.Runtime.Builder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestModuleTest extends PousseCafeTest {

    @Override
    protected Builder runtimeBuilder() {
        return super.runtimeBuilder()
                .withBundle(new BundleConfigurer.Builder()
                .module(TestModule.class)
                .build()
                .defineAndImplementDefault()
                .build());
    }

    @Test
    public void waitUntilEmptyOrInterruptedReturnsTrueWhenAllMessagesIssued() {
        givenMessages();
        whenEvents(messages);
        thenAllRecordedAfterWait();
    }

    private void givenMessages() {
        for(int i = 0; i < 1000; ++i) {
            messages.add(new SampleMessage());
        }
    }

    private List<SampleMessage> messages = new ArrayList<>();

    private void thenAllRecordedAfterWait() {
        waitUntilAllMessageQueuesEmpty();
        var recordedMessages = capturedMessages(SampleMessage.class);
        assertThat(recordedMessages.size(), is(messages.size()));
    }
}
