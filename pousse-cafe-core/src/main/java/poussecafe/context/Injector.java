package poussecafe.context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;

public class Injector {

    private Map<Class<?>, Object> injectableServices = new HashMap<>();

    private Set<Object> injectionCandidates = new HashSet<>();

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
        tryUsingSetters(service);
        tryUsingMembers(service);
    }

    private void tryUsingSetters(Object service) {
        for (Method method : service.getClass().getMethods()) {
            SetterDependencyInjector dependencyInjector = new SetterDependencyInjector(service, method, injectableServices);
            dependencyInjector.trySettingDependency();
        }
    }

    private void tryUsingMembers(Object service) {
        for(Field field : getAllMembers(service)) {
            FieldDependencyInjector dependencyInjector = new FieldDependencyInjector(service, field, injectableServices);
            dependencyInjector.trySettingDependency();
        }
    }

    private List<Field> getAllMembers(Object service) {
        List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = service.getClass();
        while(currentClass != null && currentClass != Object.class) {
            allFields.addAll(asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }
}
