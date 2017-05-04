package poussecafe.journal;

import java.util.ArrayList;
import java.util.List;
import poussecafe.messaging.Message;

public class ConsumptionFailureBuilder {

    private Message message;

    private List<String> listenerIds;

    public ConsumptionFailureBuilder(Message message) {
        this.message = message;
        listenerIds = new ArrayList<>();
    }

    public void addListener(String listenerId) {
        listenerIds.add(listenerId);
    }

    public ConsumptionFailure build() {
        return new ConsumptionFailure(message, listenerIds);
    }

}
