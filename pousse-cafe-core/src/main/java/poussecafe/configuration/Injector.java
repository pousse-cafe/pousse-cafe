package poussecafe.configuration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Injector {

    private Map<Class<?>, Object> injectableServices;

    private Set<Object> injectionCandidates;

    public Injector() {
        injectableServices = new HashMap<>();
        injectionCandidates = new HashSet<>();
    }

    public void registerInjectableService(Object service) {
        injectableServices.put(service.getClass(), service);
    }

    public void addInjectionCandidate(Object candidate) {
        injectionCandidates.add(candidate);
    }

    public void injectDependencies() {
        for (Object service : injectionCandidates) {
            injectDependencies(service);
        }
    }

    private void injectDependencies(Object service) {
        for (Method method : service.getClass().getMethods()) {
            DependencySetter dependencySetter = new DependencySetter(service, method, injectableServices);
            dependencySetter.trySettingDependency();
        }
    }

}
