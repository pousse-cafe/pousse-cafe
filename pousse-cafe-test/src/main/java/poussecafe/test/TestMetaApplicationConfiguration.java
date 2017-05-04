package poussecafe.test;

import java.time.Duration;
import poussecafe.configuration.MetaApplicationConfiguration;
import poussecafe.journal.PollingPeriod;
import poussecafe.messaging.MessageReceiver;
import poussecafe.messaging.InMemoryMessageQueue;

public class TestMetaApplicationConfiguration extends MetaApplicationConfiguration {

    @Override
    public PollingPeriod getCommandWatcherPollingPeriod() {
        return PollingPeriod.withPeriod(Duration.ofMillis(10));
    }

    public void waitUntilAllMessageQueuesEmpty()
            throws InterruptedException {
        for (MessageReceiver messageReceiver : getMessageReceivers()) {
            InMemoryMessageQueue queue = (InMemoryMessageQueue) messageReceiver;
            queue.waitUntilEmpty();
        }
    }
}
