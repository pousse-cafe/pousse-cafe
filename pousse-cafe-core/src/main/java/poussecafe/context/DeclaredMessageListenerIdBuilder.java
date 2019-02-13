package poussecafe.context;

import java.lang.reflect.Method;
import java.util.Objects;
import poussecafe.messaging.Message;

public class DeclaredMessageListenerIdBuilder {

    public DeclaredMessageListenerIdBuilder() {

    }

    public DeclaredMessageListenerIdBuilder messageClass(Class<? extends Message> messageClass) {
        this.messageClass = messageClass;
        return this;
    }

    private Class<? extends Message> messageClass;

    public DeclaredMessageListenerIdBuilder method(Method method) {
        this.method = method;
        return this;
    }

    private Method method;

    public DeclaredMessageListenerIdBuilder target(Object target) {
        this.target = target;
        return this;
    }

    private Object target;

    public String build() {
        Objects.requireNonNull(messageClass);
        Objects.requireNonNull(method);
        Objects.requireNonNull(target);
        return target.getClass().getCanonicalName() + "::" + method.getName() + "(" + messageClass.getCanonicalName() + ")";
    }
}
