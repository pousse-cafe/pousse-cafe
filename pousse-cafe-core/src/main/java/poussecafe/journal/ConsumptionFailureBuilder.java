package poussecafe.journal;

import java.util.ArrayList;
import java.util.List;
import poussecafe.consequence.Consequence;

public class ConsumptionFailureBuilder {

    private Consequence consequence;

    private List<String> listenerIds;

    public ConsumptionFailureBuilder(Consequence consequence) {
        this.consequence = consequence;
        listenerIds = new ArrayList<>();
    }

    public void addListener(String listenerId) {
        listenerIds.add(listenerId);
    }

    public ConsumptionFailure build() {
        return new ConsumptionFailure(consequence, listenerIds);
    }

}
