package poussecafe.util;

import java.util.Objects;
import java.util.function.Supplier;
import org.slf4j.Logger;
import poussecafe.exception.PousseCafeException;

import static org.slf4j.LoggerFactory.getLogger;

public class Poller {

    public Poller(Supplier<Boolean> poll) {
        Objects.requireNonNull(poll);
        this.poll = poll;
    }

    private Supplier<Boolean> poll;

    public void waitForCondition() {
        if(poll.get()) {
            return;
        } else {
            runPolling();
        }
    }

    private void runPolling() {
        long currentWait = 10;
        try {
            Thread.sleep(currentWait);
        } catch (InterruptedException e) {
            throw new PousseCafeException("Poller was interrupted");
        }

        int maxRetries = 10;
        for(int i = 0; i < maxRetries; ++i) {
            boolean conditionValue = poll.get();
            if(conditionValue) {
                return;
            } else {
                currentWait = currentWait * 2;
                logger.debug("Condition not yet true, polling again in {} ms", currentWait);
                try {
                    Thread.sleep(currentWait);
                } catch (InterruptedException e) {
                    throw new PousseCafeException("Poller was interrupted");
                }
            }
        }

        throw new PollerTimeoutException("Condition did not get true after " + maxRetries + " retries");
    }

    private Logger logger = getLogger(getClass());
}
