package poussecafe.journal.domain;

import java.util.List;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Checks.checkThatValue;
import static poussecafe.check.Predicates.emptyOrNullCollection;
import static poussecafe.check.Predicates.not;

public class ConsumptionFailure {

    public ConsumptionFailure(ConsumptionFailureKey key, List<String> listenerIds) {
        setKey(key);
        setListenerIds(listenerIds);
    }

    private void setKey(ConsumptionFailureKey key) {
        checkThatValue(key).notNull();
        this.key = key;
    }

    private ConsumptionFailureKey key;

    public ConsumptionFailureKey getKey() {
        return key;
    }

    private void setListenerIds(List<String> listenerIds) {
        checkThat(value(listenerIds)
                .verifies(not(emptyOrNullCollection()))
                .because("There must be at least 1 listener ID"));
        this.listenerIds = listenerIds;
    }

    private List<String> listenerIds;

    public List<String> getListenerIds() {
        return listenerIds;
    }
}
