package poussecafe.source.model;

import java.util.List;
import poussecafe.discovery.DefaultProcess;
import poussecafe.source.resolution.ResolvedMethod;
import poussecafe.source.resolution.ResolvedTypeName;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class MessageListenerSource {

    public MessageListenerContainer container() {
        return container;
    }

    private MessageListenerContainer container;

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

    public List<ProducedEvent> producedEvents() {
        return producedEvents;
    }

    private List<ProducedEvent> producedEvents;

    public static class Builder {

        private MessageListenerSource messageListener = new MessageListenerSource();

        public MessageListenerSource build() {
            requireNonNull(messageListener.container);
            requireNonNull(messageListener.methodName);
            requireNonNull(messageListener.messageName);
            requireNonNull(messageListener.processNames);
            return messageListener;
        }

        public Builder withContainer(MessageListenerContainer container) {
            messageListener.container = container;
            return this;
        }

        public Builder withMethodDeclaration(ResolvedMethod method) {
            var annotatedMethod = method.asAnnotatedElement();
            String methodName = method.name();
            if(!MessageListenerAnnotations.isMessageListener(annotatedMethod)) {
                throw new IllegalArgumentException("Method " + methodName + " is not a message listener");
            }

            messageListener.methodName = methodName;
            messageListener.messageName = method.parameterTypeName(0).orElseThrow().simpleName();

            MessageListenerAnnotations messageListenerAnnotation = new MessageListenerAnnotations(annotatedMethod);
            List<ResolvedTypeName> processes = messageListenerAnnotation.processes();
            if(processes.isEmpty()) {
                messageListener.processNames = singletonList(DefaultProcess.class.getSimpleName());
            } else {
                messageListener.processNames = processes.stream()
                        .map(ResolvedTypeName::simpleName)
                        .collect(toList());
            }

            messageListener.producedEvents = messageListenerAnnotation.producedEvents().stream()
                    .map(annotation -> new ProducedEvent.Builder()
                            .eventName(annotation.event().simpleName())
                            .required(annotation.required().orElse(true))
                            .build())
                    .collect(toList());

            return this;
        }
    }
}
