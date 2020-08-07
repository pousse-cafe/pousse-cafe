package poussecafe.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Checks that a given subject class has at least a given set of expected type arguments set in its superclass
 * or interfaces parametrized types. The check is applied recursively at each level of the class hierarchy (following
 * superclasses only) until all expected type arguments are found or the top of the hierarchy is reached. If all
 * expected type arguments are not found, an exception is thrown.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ClassHierarchyTypeArgumentsValidator {

    public void validOrThrow() {
        validOrThrow(subject.getSuperclass(), subject.getGenericSuperclass(), subject.getGenericInterfaces());
    }

    private void validOrThrow(Class runnerClass, Type runnerType, Type[] runnerInterfaces) {
        if(runnerClass == null) {
            throw new IllegalArgumentException("All expected type argument values were not found");
        } else {
            considerType(runnerType);
            for(Type runnerInterface : runnerInterfaces) {
                considerType(runnerInterface);
            }
            if(!allExpectedTypeArgumentValuesFound()) {
                validOrThrow(runnerClass.getSuperclass(), runnerClass.getGenericSuperclass(), runnerClass.getGenericInterfaces());
            }
        }
    }

    private void considerType(Type runnerType) {
        if(runnerType instanceof ParameterizedType) {
            ParameterizedType parametrizedRunner = (ParameterizedType) runnerType;
            considerTypeArguments(parametrizedRunner);
        }
    }

    private void considerTypeArguments(ParameterizedType parametrizedRunner) {
        for(int i = 0; i < parametrizedRunner.getActualTypeArguments().length; ++i) {
            Type actualTypeArgument = parametrizedRunner.getActualTypeArguments()[i];
            if(actualTypeArgument instanceof Class) {
                considerTypeArgument((Class) actualTypeArgument);
            }
        }
    }

    private void considerTypeArgument(Class actualTypeArgument) {
        for(int i = 0; i < expectedTypeArguments.length; ++i) {
            Class expectedTypeArgumentValue = expectedTypeArguments[i];
            if(expectedTypeArgumentValue.isAssignableFrom(actualTypeArgument)) {
                found[i] = true;
            }
        }
    }

    private boolean allExpectedTypeArgumentValuesFound() {
        for(int i = 0; i < found.length; ++i) {
            if(!found[i]) {
                return false;
            }
        }
        return true;
    }

    private Class subject;

    private String listenerId;

    private Class[] expectedTypeArguments;

    private boolean[] found;

    public static class Builder {

        private ClassHierarchyTypeArgumentsValidator validator = new ClassHierarchyTypeArgumentsValidator();

        public ClassHierarchyTypeArgumentsValidator build() {
            requireNonNull(validator.subject);
            requireNonNull(validator.listenerId);
            if(expectedTypeArguments.isEmpty()) {
                throw new IllegalStateException("No expected type argument provided");
            }

            validator.expectedTypeArguments = new Class[expectedTypeArguments.size()];
            expectedTypeArguments.toArray(validator.expectedTypeArguments);
            validator.found = new boolean[expectedTypeArguments.size()];

            return validator;
        }

        public Builder runner(Class runner) {
            validator.subject = runner;
            return this;
        }

        public Builder listenerId(String listenerId) {
            validator.listenerId = listenerId;
            return this;
        }

        public Builder expectedTypeArgument(Class expectedTypeArgument) {
            expectedTypeArguments.add(expectedTypeArgument);
            return this;
        }

        private List<Class> expectedTypeArguments = new ArrayList<>();
    }

    private ClassHierarchyTypeArgumentsValidator() {

    }
}
