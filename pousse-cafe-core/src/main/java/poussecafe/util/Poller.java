package poussecafe.util;

import java.time.Duration;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import org.slf4j.Logger;
import poussecafe.domain.DomainException;
import poussecafe.exception.PousseCafeException;

import static org.slf4j.LoggerFactory.getLogger;

public class Poller {

    public static class Builder {

        private Poller poller = new Poller();

        public Builder condition(BooleanSupplier condition) {
            poller.condition = condition;
            return this;
        }

        public Builder initialWaitTime(Duration initialWaitTime) {
            poller.initialWaitTime = initialWaitTime;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            poller.maxRetries = maxRetries;
            return this;
        }

        public Builder backOffFactor(int backOffFactor) {
            poller.backOffFactor = backOffFactor;
            return this;
        }

        public Builder maxWaitTime(Duration maxWaitTime) {
            poller.maxWaitTime = maxWaitTime;
            return this;
        }

        public Poller build() {
            Objects.requireNonNull(poller.condition);

            Objects.requireNonNull(poller.initialWaitTime);
            if(poller.initialWaitTime.isZero() || poller.initialWaitTime.isNegative()) {
                throw new DomainException("Initial wait time must be positive");
            }

            if(poller.maxRetries <= 0) {
                throw new DomainException("Max retries must be positive");
            }

            if(poller.backOffFactor <= 0) {
                throw new DomainException("Back-off factor must be positive");
            }

            Objects.requireNonNull(poller.maxWaitTime);
            if(poller.maxWaitTime.isZero() || poller.maxWaitTime.isNegative()) {
                throw new DomainException("Max. wait time must be positive");
            }

            return poller;
        }
    }

    private Poller() {

    }

    private BooleanSupplier condition;

    private Duration initialWaitTime = Duration.ofMillis(10);

    private int maxRetries = 10;

    private double backOffFactor = 2;

    private Duration maxWaitTime = Duration.ofSeconds(10);

    public void waitForCondition() {
        if(!condition.getAsBoolean()) {
            runPolling();
        }
    }

    private void runPolling() {
        long currentWait = Math.min(initialWaitTime.toMillis(), maxWaitTime.toMillis());
        waitAndHandleInterrupt(currentWait);
        for(int i = 0; i < maxRetries; ++i) {
            boolean conditionValue = condition.getAsBoolean();
            if(conditionValue) {
                return;
            } else {
                currentWait = Math.min((long)(currentWait * backOffFactor), maxWaitTime.toMillis());
                logger.debug("Condition not yet true, polling again in {} ms", currentWait);
                waitAndHandleInterrupt(currentWait);
            }
        }

        throw new PollerTimeoutException("Condition did not get true after " + maxRetries + " retries");
    }

    private void waitAndHandleInterrupt(long currentWait) {
        try {
            Thread.sleep(currentWait);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PousseCafeException("Poller was interrupted");
        }
    }

    private Logger logger = getLogger(getClass());
}
