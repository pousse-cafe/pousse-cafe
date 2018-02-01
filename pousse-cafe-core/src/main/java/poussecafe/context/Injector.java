package poussecafe.context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.util.ReflectionUtils;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class Injector {

    private Map<Class<?>, Object> injectableServices = new HashMap<>();

    private Set<Object> injectionCandidates = new HashSet<>();

    public void registerInjectableService(Object service) {
        injectableServices.put(service.getClass(), service);
    }

    public void registerInjectableService(Class<?> forClass, Object service) {
        checkThat(value(forClass.isAssignableFrom(service.getClass())).isTrue().because("Service must subclass forClass"));
        injectableServices.put(forClass, service);
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
        logger.info("Injecting dependencies in " + service.getClass().getName());
        tryUsingSetters(service);
        tryUsingMembers(service);
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private void tryUsingSetters(Object service) {
        for (Method method : service.getClass().getMethods()) {
            SetterDependencyInjector dependencyInjector = new SetterDependencyInjector(service, method, injectableServices);
            dependencyInjector.trySettingDependency();
        }
    }

    private void tryUsingMembers(Object service) {
        for(Field field : ReflectionUtils.getHierarchyFields(service.getClass())) {
            FieldDependencyInjector dependencyInjector = new FieldDependencyInjector(service, field, injectableServices);
            dependencyInjector.trySettingDependency();
        }
    }
}
