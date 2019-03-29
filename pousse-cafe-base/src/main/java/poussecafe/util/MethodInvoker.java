package poussecafe.util;

import java.lang.reflect.Method;
import java.util.Objects;
import poussecafe.exception.PousseCafeException;

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

        public MethodInvoker build() {
            Objects.requireNonNull(invoker.method);
            Objects.requireNonNull(invoker.target);
            return invoker;
        }
    }

    private MethodInvoker() {

    }

    public Object invoke(Object... args) {
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new PousseCafeException("Unable to invoke method", e);
        }
    }

    private Method method;

    private Object target;
}
