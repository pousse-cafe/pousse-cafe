package poussecafe.consequence;

import java.time.LocalDateTime;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class ScheduledConsequence {

    public static ScheduledConsequence schedule(Consequence consequence) {
        ScheduledConsequence scheduledEvent = new ScheduledConsequence();
        scheduledEvent.setConsequence(consequence);
        return scheduledEvent;
    }

    private Consequence consequence;

    private LocalDateTime scheduledDate;

    private ScheduledConsequence() {

    }

    private void setConsequence(Consequence consequence) {
        checkThat(value(consequence).notNull().because("Consequence cannot be null"));
        this.consequence = consequence;
    }

    public void on(LocalDateTime scheduledDate) {
        checkThat(value(scheduledDate).notNull().because("Scheduled date cannot be null"));
        this.scheduledDate = scheduledDate;
    }

    public Consequence getConsequence() {
        return consequence;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }
}
