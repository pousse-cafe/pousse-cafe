package poussecafe.journal;

import java.util.List;
import poussecafe.messaging.Message;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.emptyOrNullCollection;
import static poussecafe.check.Predicates.not;

public class ConsumptionFailure {

    private Message message;

    private List<String> listenerIds;

    public ConsumptionFailure(Message message, List<String> listenerIds) {
        setMessage(message);
        setListenerIds(listenerIds);
    }

    public Message getMessage() {
        return message;
    }

    private void setMessage(Message message) {
        checkThat(value(message).notNull().because("Message cannot be null"));
        this.message = message;
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
