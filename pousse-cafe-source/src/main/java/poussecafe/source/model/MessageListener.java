package poussecafe.source.model;

import java.util.List;
import java.util.Optional;
import poussecafe.discovery.DefaultProcess;
import poussecafe.source.analysis.MessageListenerAnnotations;
import poussecafe.source.analysis.ResolvedMethod;
import poussecafe.source.analysis.ResolvedTypeName;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class MessageListener {

    public MessageListenerContainer container() {
        return container;
    }

    private MessageListenerContainer container;

    public String methodName() {
        return methodName;
    }

    private String methodName;

    public Message consumedMessage() {
        return consumedMessage;
    }

    private Message consumedMessage;

    public List<String> processNames() {
        return processNames;
    }

    private List<String> processNames;

    public List<ProducedEvent> producedEvents() {
        return producedEvents;
    }

    private List<ProducedEvent> producedEvents;

    public Optional<String> runnerName() {
        return runnerName;
    }

    private Optional<String> runnerName;

    public Optional<String> consumesFromExternal() {
        return consumesFromExternal;
    }

    private Optional<String> consumesFromExternal;

    public static class Builder {

        private MessageListener messageListener = new MessageListener();

        public MessageListener build() {
            requireNonNull(messageListener.container);
            requireNonNull(messageListener.methodName);
            requireNonNull(messageListener.consumedMessage);
            requireNonNull(messageListener.processNames);
            requireNonNull(messageListener.consumesFromExternal);
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
            messageListener.consumedMessage = Message.ofTypeName(method.parameterTypeName(0).orElseThrow());

            MessageListenerAnnotations messageListenerAnnotation = new MessageListenerAnnotations(annotatedMethod);
            messageListener.consumesFromExternal = messageListenerAnnotation.consumesFromExternal();
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
                            .message(Message.ofTypeName(annotation.event()))
                            .required(annotation.required().orElse(true))
                            .consumedByExternal(annotation.consumedByExternal())
                            .build())
                    .collect(toList());

            messageListener.runnerName = messageListenerAnnotation.runner().map(ResolvedTypeName::simpleName);

            return this;
        }
    }
}
