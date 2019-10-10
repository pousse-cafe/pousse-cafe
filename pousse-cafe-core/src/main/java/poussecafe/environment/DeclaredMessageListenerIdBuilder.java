package poussecafe.environment;

import java.lang.reflect.Method;
import java.util.Objects;
import poussecafe.messaging.Message;

public class DeclaredMessageListenerIdBuilder {

    public DeclaredMessageListenerIdBuilder messageClass(Class<? extends Message> messageClass) {
        this.messageClass = messageClass;
        return this;
    }

    private Class<?> messageClass;

    public DeclaredMessageListenerIdBuilder method(Method method) {
        declaringClass = method.getDeclaringClass();
        methodName = method.getName();
        return this;
    }

    private Class<?> declaringClass;

    private String methodName;

    public DeclaredMessageListenerIdBuilder declaringClass(Class<?> declaringClass) {
        this.declaringClass = declaringClass;
        return this;
    }

    public DeclaredMessageListenerIdBuilder methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public String build() {
        checkRequirements();
        return declaringClass.getName() + "::" + methodName + "(" + messageClass.getName() + ")";
    }

    private void checkRequirements() {
        Objects.requireNonNull(declaringClass);
        Objects.requireNonNull(methodName);
        Objects.requireNonNull(messageClass);
    }

    public String buildShortId() {
        checkRequirements();
        return declaringClass.getSimpleName() + "::" + methodName + "(" + messageClass.getSimpleName() + ")";
    }
}
