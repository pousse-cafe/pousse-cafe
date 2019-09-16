package poussecafe.injector;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.exception.PousseCafeException;

public class FieldDependencyInjector extends DependencyInjector {

    private Object service;

    private Field field;

    public FieldDependencyInjector(Object service, Field field, Map<Class<?>, Object> injectableServices) {
        super(injectableServices);
        setService(service);
        setField(field);
    }

    private void setService(Object service) {
        Objects.requireNonNull(service);
        this.service = service;
    }

    private void setField(Field field) {
        Objects.requireNonNull(field);
        this.field = field;
    }

    @Override
    protected boolean isValidTarget() {
        return field.getGenericType() instanceof Class;
    }

    @Override
    protected Class<?> getTargetType() {
        return (Class<?>) field.getGenericType();
    }

    @Override
    protected void setResolvedDependency(Object dependency) {
        try {
            makeAccessibleIfPrivate();
            if(field.get(service) == null) {
                field.set(service, dependency);
            } else {
                logger.debug("Ignoring field {} (already set)", field.getName());
            }
            closeAgainIfPrivate();
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new PousseCafeException("Unable to inject dependency " + dependency + " in field " + field.getName(), e);
        }
    }

    private void makeAccessibleIfPrivate() {
        setAccessibleFlagIfPrivate(true);
    }

    private void closeAgainIfPrivate() {
        setAccessibleFlagIfPrivate(false);
    }

    private void setAccessibleFlagIfPrivate(boolean accessible) {
        if(!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(accessible);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());
}
