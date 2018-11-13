package poussecafe.property;

import static poussecafe.check.Checks.checkThatValue;

public class ClassAccessRule implements AllowRule {

    public ClassAccessRule(Class<?> allowedClass) {
        checkThatValue(allowedClass).notNull();
        this.allowedClass = allowedClass;
    }

    private Class<?> allowedClass;

    @Override
    public boolean allow(Object accessor) {
        return allowedClass.equals(accessor.getClass());
    }

}
