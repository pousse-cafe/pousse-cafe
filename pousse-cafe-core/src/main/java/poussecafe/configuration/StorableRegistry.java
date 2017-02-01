package poussecafe.configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import poussecafe.exception.PousseCafeException;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.hasKey;
import static poussecafe.check.Predicates.not;

public class StorableRegistry {

    private Map<Class<?>, StorableServices> entries;

    public StorableRegistry() {
        entries = new HashMap<>();
    }

    public void registerServices(StorableServices entry) {
        checkThat(value(entries).verifies(not(hasKey(entry.getStorableClass()))).because(
                "Cannot register services for a storable serveral times"));
        entries.put(entry.getStorableClass(), entry);
    }

    public StorableServices getServices(Class<?> storableClass) {
        StorableServices services = entries.get(storableClass);
        if (services == null) {
            throw new PousseCafeException("No services registered for given class");
        }
        return services;
    }

    public Collection<StorableServices> getStorablesServices() {
        return entries.values();
    }
}
