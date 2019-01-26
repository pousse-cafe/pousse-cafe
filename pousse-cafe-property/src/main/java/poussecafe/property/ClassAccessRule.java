package poussecafe.property;

import java.util.Objects;

public class ClassAccessRule implements AllowRule {

    public ClassAccessRule(Class<?> allowedClass) {
        Objects.requireNonNull(allowedClass);
        this.allowedClass = allowedClass;
    }

    private Class<?> allowedClass;

    @Override
    public boolean allow(Object accessor) {
        return allowedClass.equals(accessor.getClass());
    }

}
