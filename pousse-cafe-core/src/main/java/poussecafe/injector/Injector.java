package poussecafe.injector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.exception.PousseCafeException;
import poussecafe.util.ReflectionUtils;

import static java.util.stream.Collectors.toList;

public class Injector {

    public static class Builder {

        private Injector injector = new Injector();

        public Builder registerInjectableService(Object service) {
            Objects.requireNonNull(service);
            injector.injectableServices.put(service.getClass(), service);
            return this;
        }

        public Builder registerInjectableService(Class<?> forClass, Object service) {
            if(!forClass.isAssignableFrom(service.getClass())) {
                throw new PousseCafeException("Service must subclass forClass");
            }
            injector.injectableServices.put(forClass, service);
            return this;
        }

        public Injector build() {
            return injector;
        }
    }

    private Injector() {

    }

    private Map<Class<?>, Object> injectableServices = new HashMap<>();

    public void injectDependenciesInto(Object component) {
        logger.debug("Injecting dependencies in {}", component.getClass().getName());
        tryUsingSetters(component);
        tryUsingMembers(component);
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

    public Collection<Object> injectableServices() {
        return injectableServices.values().stream()
                .distinct()
                .collect(toList());
    }
}
