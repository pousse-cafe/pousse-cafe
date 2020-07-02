package poussecafe.source.model;

import java.util.List;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import poussecafe.discovery.MessageListener;
import poussecafe.source.resolution.AnnotatedElement;
import poussecafe.source.resolution.ResolvedMethod;

import static java.util.Objects.requireNonNull;

public class MessageListenerSource {

    public String methodName() {
        return methodName;
    }

    private String methodName;

    public String messageName() {
        return messageName;
    }

    private String messageName;

    public List<String> processNames() {
        return processNames;
    }

    private List<String> processNames;

    public static class Builder {

        private MessageListenerSource source = new MessageListenerSource();

        public MessageListenerSource build() {
            requireNonNull(source.methodName);
            requireNonNull(source.messageName);
            requireNonNull(source.processNames);
            return source;
        }

        public Builder withMethodDeclaration(ResolvedMethod method) {
            var annotatedMethod = method.asAnnotatedElement();
            String methodName = method.name();
            if(!isMessageListener(annotatedMethod)) {
                throw new IllegalArgumentException("Method " + methodName + " is not a message listener");
            }

            source.methodName = methodName;
            source.messageName = method.parameterTypeName(0).orElseThrow().simpleName();

            MessageListenerAnnotation messageListenerAnnotation = new MessageListenerAnnotation(annotatedMethod);
            source.processNames = messageListenerAnnotation.processNames();

            return this;
        }
    }

    public static boolean isMessageListener(AnnotatedElement<MethodDeclaration> annotatedElement) {
        return annotatedElement.findAnnotation(MessageListener.class).isPresent();
    }
}
