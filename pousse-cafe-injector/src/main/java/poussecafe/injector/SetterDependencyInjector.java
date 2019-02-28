package poussecafe.injector;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import poussecafe.exception.PousseCafeException;

public class SetterDependencyInjector extends DependencyInjector {

    private Object service;

    private Method method;

    public SetterDependencyInjector(Object service, Method method, Map<Class<?>, Object> injectableServices) {
        super(injectableServices);
        setService(service);
        setMethod(method);
    }

    private void setService(Object service) {
        Objects.requireNonNull(service);
        this.service = service;
    }

    private void setMethod(Method method) {
        Objects.requireNonNull(method);
        this.method = method;
    }

    @Override
    protected boolean isValidTarget() {
        return method.getName().startsWith("set") && method.getParameterCount() == 1;
    }

    @Override
    protected Class<?> getTargetType() {
        return method.getParameters()[0].getType();
    }

    @Override
    protected void setResolvedDependency(Object dependency) {
        try {
            method.invoke(service, dependency);
        } catch (Exception e) {
            throw new PousseCafeException("Unable to set dependency " + dependency.getClass().getName(), e);
        }
    }
}
