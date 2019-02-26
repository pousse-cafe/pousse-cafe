package poussecafe.runtime;

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

    public String build() {
        Objects.requireNonNull(messageClass);
        Objects.requireNonNull(method);
        return method.getDeclaringClass().getCanonicalName() + "::" + method.getName() + "(" + messageClass.getCanonicalName() + ")";
    }
}
