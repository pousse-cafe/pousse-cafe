package poussecafe.journal;

import java.time.Duration;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class PollingPeriod {

    public static final PollingPeriod DEFAULT_POLLING_PERIOD = PollingPeriod.withPeriod(Duration.ofMillis(500));

    private Duration period;

    private PollingPeriod(Duration period) {
        setPeriod(period);
    }

    private void setPeriod(Duration period) {
        checkThat(value(period).verifies(this::validPollingPeriod).because("Polling period must be valid"));
        this.period = period;
    }

    private boolean validPollingPeriod(Duration pollingPeriod) {
        return pollingPeriod != null && !pollingPeriod.isNegative() && !pollingPeriod.isZero();
    }

    public static PollingPeriod withPeriod(Duration period) {
        return new PollingPeriod(period);
    }

    public long toMillis() {
        return period.toMillis();
    }
}
