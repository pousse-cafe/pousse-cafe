package poussecafe.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("rawtypes")
public class MessageListenerConsistencyValidator {

    public void include(MessageListener listener) {
        Optional<Class> aggregateRootClass = listener.aggregateRootClass();
        if(aggregateRootClass.isPresent()) {
            AggregateListeners aggregateListeners = aggregatesListeners.computeIfAbsent(aggregateRootClass.get(), key -> new AggregateListeners());
            aggregateListeners.include(listener);
        }
    }

    private Map<Class, AggregateListeners> aggregatesListeners = new HashMap<>();
}
