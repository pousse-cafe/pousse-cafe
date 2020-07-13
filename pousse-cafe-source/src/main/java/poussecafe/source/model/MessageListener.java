package poussecafe.source.model;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import poussecafe.discovery.DefaultProcess;
import poussecafe.source.analysis.MessageListenerAnnotations;
import poussecafe.source.analysis.ResolvedMethod;
import poussecafe.source.analysis.ResolvedType;
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

    public Optional<ProductionType> productionType() {
        return productionType;
    }

    private Optional<ProductionType> productionType;

    public static class Builder {

        private MessageListener messageListener = new MessageListener();

        public MessageListener build() {
            requireNonNull(messageListener.container);
            requireNonNull(messageListener.methodName);
            requireNonNull(messageListener.consumedMessage);
            requireNonNull(messageListener.processNames);
            requireNonNull(messageListener.consumesFromExternal);

            if(messageListener.container.type() == MessageListenerContainerType.FACTORY
                    && messageListener.productionType.isEmpty()) {
                throw new IllegalStateException("Production type must be present with factory listeners");
            }

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
                            .withAnnotation(annotation)
                            .build())
                    .collect(toList());

            messageListener.runnerName = messageListenerAnnotation.runner().map(ResolvedTypeName::simpleName);

            Optional<ResolvedType> returnType = method.returnType();
            if(returnType.isPresent()
                    && !returnType.get().isPrimitive()) {
                messageListener.productionType = Optional.of(productionType(returnType.get()));
            }

            return this;
        }

        private ProductionType productionType(ResolvedType returnType) {
            ResolvedTypeName typeName = returnType.genericTypeName();
            if(typeName.instanceOf(Collection.class)) {
                return ProductionType.SEVERAL;
            } else if(typeName.instanceOf(Optional.class)) {
                return ProductionType.OPTIONAL;
            } else {
                return ProductionType.SINGLE;
            }
        }
    }
}
