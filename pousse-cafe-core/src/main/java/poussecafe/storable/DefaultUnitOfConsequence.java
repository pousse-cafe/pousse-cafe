package poussecafe.storable;

import java.util.ArrayList;
import java.util.List;
import poussecafe.consequence.Consequence;
import poussecafe.consequence.ScheduledConsequence;

import static java.util.Collections.unmodifiableList;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class DefaultUnitOfConsequence implements UnitOfConsequence {

    private List<Consequence> consequences = new ArrayList<>();

    private List<ScheduledConsequence> scheduledConsequences = new ArrayList<>();

    @Override
    public void addConsequence(Consequence event) {
        checkThat(value(event).notNull().because("Consequence cannot be null"));
        consequences.add(event);
    }

    @Override
    public List<Consequence> getConsequences() {
        return unmodifiableList(consequences);
    }

    @Override
    public void scheduledConsequence(ScheduledConsequence scheduledConsequence) {
        checkThat(value(scheduledConsequence).notNull().because("Scheduled consequence cannot be null"));
        scheduledConsequences.add(scheduledConsequence);
    }

    @Override
    public List<ScheduledConsequence> getScheduledConsequences() {
        return unmodifiableList(scheduledConsequences);
    }

}
