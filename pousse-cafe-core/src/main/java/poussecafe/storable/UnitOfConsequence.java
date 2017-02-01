package poussecafe.storable;

import java.util.List;
import poussecafe.consequence.Consequence;
import poussecafe.consequence.ScheduledConsequence;

public interface UnitOfConsequence {

    void addConsequence(Consequence consequence);

    List<Consequence> getConsequences();

    void scheduledConsequence(ScheduledConsequence scheduledConsequence);

    List<ScheduledConsequence> getScheduledConsequences();
}
