package poussecafe.configuration;

import java.lang.reflect.Method;
import java.util.Map;
import poussecafe.exception.PousseCafeException;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class DependencySetter {

    private Map<Class<?>, Object> injectableServices;

    private Object service;

    private Method method;

    public DependencySetter(Object service, Method method, Map<Class<?>, Object> injectableServices) {
        setService(service);
        setMethod(method);
        setInjectableServices(injectableServices);
    }

    private void setService(Object service) {
        checkThat(value(service).notNull().because("Service cannot be null"));
        this.service = service;
    }

    private void setMethod(Method method) {
        checkThat(value(method).notNull().because("Method cannot be null"));
        this.method = method;
    }

    private void setInjectableServices(Map<Class<?>, Object> injectableServices) {
        checkThat(value(injectableServices).notNull().because("Injectable services cannot be null"));
        this.injectableServices = injectableServices;
    }

    public void trySettingDependency() {
        if (isMethodDependencySetter()) {
            setDependencyIfResolved();
        }
    }

    private boolean isMethodDependencySetter() {
        return method.getName().startsWith("set") && method.getParameterCount() == 1;
    }

    private void setDependencyIfResolved() {
        Class<?> dependencyType = method.getParameters()[0].getType();
        Object dependency = injectableServices.get(dependencyType);
        if (dependency != null) {
            setResolvedDependency(dependency);
        }
    }

    protected void setResolvedDependency(Object dependency) {
        try {
            method.invoke(service, dependency);
        } catch (Exception e) {
            throw new PousseCafeException("Unable to set dependency " + dependency.getClass().getName(), e);
        }
    }
}
