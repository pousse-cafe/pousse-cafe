package poussecafe.journal;

import java.util.List;
import poussecafe.consequence.Consequence;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.emptyOrNullCollection;
import static poussecafe.check.Predicates.not;

public class ConsumptionFailure {

    private Consequence consequence;

    private List<String> listenerIds;

    public ConsumptionFailure(Consequence consequence, List<String> listenerIds) {
        setConsequence(consequence);
        setListenerIds(listenerIds);
    }

    public Consequence getConsequence() {
        return consequence;
    }

    private void setConsequence(Consequence consequence) {
        checkThat(value(consequence).notNull().because("Consequence cannot be null"));
        this.consequence = consequence;
    }

    public List<String> getListenerIds() {
        return listenerIds;
    }

    private void setListenerIds(List<String> listenerIds) {
        checkThat(value(listenerIds)
                .verifies(not(emptyOrNullCollection()))
                .because("There must be at least 1 listener ID"));
        this.listenerIds = listenerIds;
    }
}
