package poussecafe.environment;

import java.lang.reflect.Method;
import java.util.Objects;
import poussecafe.messaging.Message;

public class DeclaredMessageListenerIdBuilder {

    public DeclaredMessageListenerIdBuilder messageClass(Class<? extends Message> messageClass) {
        messageClassName = messageClass.getName();
        return this;
    }

    private String messageClassName;

    public DeclaredMessageListenerIdBuilder method(Method method) {
        declaringClassName = method.getDeclaringClass().getName();
        methodName = method.getName();
        return this;
    }

    private String declaringClassName;

    private String methodName;

    public DeclaredMessageListenerIdBuilder declaringClassName(String declaringClassName) {
        this.declaringClassName = declaringClassName;
        return this;
    }

    public DeclaredMessageListenerIdBuilder methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public DeclaredMessageListenerIdBuilder messageClassName(String messageClassName) {
        this.messageClassName = messageClassName;
        return this;
    }

    public String build() {
        Objects.requireNonNull(declaringClassName);
        Objects.requireNonNull(methodName);
        Objects.requireNonNull(messageClassName);
        return declaringClassName + "::" + methodName + "(" + messageClassName + ")";
    }
}
