package poussecafe.test;

import java.time.Duration;
import poussecafe.configuration.MetaApplicationConfiguration;
import poussecafe.consequence.ConsequenceReceiver;
import poussecafe.consequence.InMemoryConsequenceQueue;
import poussecafe.journal.PollingPeriod;

public class TestMetaApplicationConfiguration extends MetaApplicationConfiguration {

    @Override
    public PollingPeriod getCommandWatcherPollingPeriod() {
        return PollingPeriod.withPeriod(Duration.ofMillis(10));
    }

    public void waitUntilAllConsequenceQueuesEmpty()
            throws InterruptedException {
        for (ConsequenceReceiver consequenceReceiver : getConsequenceReceivers()) {
            InMemoryConsequenceQueue queue = (InMemoryConsequenceQueue) consequenceReceiver;
            queue.waitUntilEmpty();
        }
    }
}
