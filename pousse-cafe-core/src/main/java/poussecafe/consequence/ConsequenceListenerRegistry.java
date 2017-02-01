package poussecafe.consequence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import poussecafe.configuration.ConsequenceListenerEntry;

import static java.util.Collections.emptySet;
import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.hasKey;
import static poussecafe.check.Predicates.not;

public class ConsequenceListenerRegistry {

    private Map<ConsequenceListenerRoutingKey, Set<ConsequenceListener>> listeners = new HashMap<>();

    public void registerDomainEventListener(ConsequenceListenerEntry entry) {
        registerListener(entry);
    }

    public void registerCommandListener(ConsequenceListenerEntry entry) {
        checkThat(value(listeners).verifies(not(hasKey(entry.getKey())))
                .because("Only one listener can be registered per command"));
        registerListener(entry);
    }

    private void registerListener(ConsequenceListenerEntry entry) {
        Set<ConsequenceListener> registeredListeners = getOrCreateSet(entry.getKey());
        registeredListeners.add(entry.getListener());
    }

    private Set<ConsequenceListener> getOrCreateSet(ConsequenceListenerRoutingKey key) {
        Set<ConsequenceListener> consequenceListeners = listeners.get(key);
        if (consequenceListeners == null) {
            consequenceListeners = new HashSet<>();
            listeners.put(key, consequenceListeners);
        }
        return consequenceListeners;
    }

    public Set<ConsequenceListener> getListeners(ConsequenceListenerRoutingKey key) {
        return Optional.ofNullable(listeners.get(key)).orElse(emptySet());
    }
}
