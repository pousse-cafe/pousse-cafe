package poussecafe.context;

import java.lang.reflect.Method;
import java.util.Map;
import poussecafe.exception.PousseCafeException;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class SetterDependencyInjector extends DependencyInjector {

    private Object service;

    private Method method;

    public SetterDependencyInjector(Object service, Method method, Map<Class<?>, Object> injectableServices) {
        super(injectableServices);
        setService(service);
        setMethod(method);
    }

    private void setService(Object service) {
        checkThat(value(service).notNull().because("Service cannot be null"));
        this.service = service;
    }

    private void setMethod(Method method) {
        checkThat(value(method).notNull().because("Method cannot be null"));
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
