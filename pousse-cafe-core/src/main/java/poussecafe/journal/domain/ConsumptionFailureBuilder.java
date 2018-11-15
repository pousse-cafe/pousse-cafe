package poussecafe.journal.domain;

import java.util.ArrayList;
import java.util.List;

import static poussecafe.check.Checks.checkThatValue;

class ConsumptionFailureBuilder {

    ConsumptionFailureBuilder(ConsumptionFailureKey key) {
        checkThatValue(key).notNull();
        this.key = key;
    }

    private ConsumptionFailureKey key;

    private List<String> listenerIds = new ArrayList<>();

    void addListener(String listenerId) {
        listenerIds.add(listenerId);
    }

    ConsumptionFailure build() {
        return new ConsumptionFailure(key, listenerIds);
    }
}
