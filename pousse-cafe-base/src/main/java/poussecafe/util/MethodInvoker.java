package poussecafe.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MethodInvoker {

    public static class Builder {

        private MethodInvoker invoker = new MethodInvoker();

        public Builder method(Method method) {
            invoker.method = method;
            return this;
        }

        public Builder target(Object target) {
            invoker.target = target;
            return this;
        }

        public Builder rethrow(Class<? extends RuntimeException> exception) {
            invoker.exceptions.add(exception);
            return this;
        }

        public MethodInvoker build() {
            Objects.requireNonNull(invoker.method);
            Objects.requireNonNull(invoker.target);
            Objects.requireNonNull(invoker.exceptions);
            return invoker;
        }
    }

    private MethodInvoker() {

    }

    public Object invoke(Object... args) {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getCause();
            if(exceptions.contains(targetException.getClass())) {
                throw (RuntimeException) targetException;
            } else {
                throw wrapException(e.getCause());
            }
        } catch (Exception e) {
            throw wrapException(e);
        }
    }

    private MethodInvokerException wrapException(Throwable e) {
        return new MethodInvokerException("Unable to invoke method", e);
    }

    private Method method;

    private Object target;

    private Set<Class<? extends Exception>> exceptions = new HashSet<>();

    public Method method() {
        return method;
    }
}
