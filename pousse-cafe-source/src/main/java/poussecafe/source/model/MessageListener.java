package poussecafe.source.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import poussecafe.discovery.DefaultProcess;
import poussecafe.source.Source;
import poussecafe.source.analysis.MessageListenerMethod;
import poussecafe.source.analysis.ResolvedType;
import poussecafe.source.analysis.ResolvedTypeName;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static poussecafe.util.Equality.referenceEquals;

@SuppressWarnings("serial")
public class MessageListener implements Serializable {

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

    private List<String> processNames = singletonList(DefaultProcess.class.getSimpleName());

    public List<ProducedEvent> producedEvents() {
        return producedEvents;
    }

    private List<ProducedEvent> producedEvents = new ArrayList<>();

    public Optional<String> runnerName() {
        return Optional.ofNullable(runnerName);
    }

    private String runnerName;

    public Optional<String> runnerClass() {
        return Optional.ofNullable(runnerClass);
    }

    private String runnerClass;

    public Optional<String> consumesFromExternal() {
        return Optional.ofNullable(consumesFromExternal);
    }

    private String consumesFromExternal;

    public Optional<ProductionType> productionType() {
        return Optional.ofNullable(productionType);
    }

    private ProductionType productionType;

    public boolean isLinkedToAggregate() {
        return container().aggregateName().isPresent();
    }

    public String aggregateName() {
        return container().aggregateName().orElseThrow();
    }

    public Source source() {
        return source;
    }

    private Source source;

    public static class Builder {

        private MessageListener messageListener = new MessageListener();

        public MessageListener build() {
            requireNonNull(messageListener.container);
            requireNonNull(messageListener.methodName);
            requireNonNull(messageListener.consumedMessage);
            requireNonNull(messageListener.source);

            if(messageListener.container.type().isFactory()
                    && messageListener.productionType == null) {
                throw new IllegalStateException("Production type must be present with factory listeners");
            }

            if(!processNames.isEmpty()) {
                messageListener.processNames = processNames;
            }

            return messageListener;
        }

        public Builder withContainer(MessageListenerContainer container) {
            messageListener.container = container;
            return this;
        }

        public Builder withMethodDeclaration(MessageListenerMethod method) {
            String methodName = method.name();

            messageListener.methodName = methodName;
            messageListener.consumedMessage = Message.ofTypeName(method.consumedMessage().orElseThrow());

            messageListener.consumesFromExternal = method.consumesFromExternal().orElse(null);
            List<ResolvedTypeName> processes = method.processes();
            processes.stream().map(ResolvedTypeName::simpleName).forEach(processNames::add);

            messageListener.producedEvents = method.producedEvents().stream()
                    .map(annotation -> new ProducedEvent.Builder()
                            .withAnnotation(annotation)
                            .build())
                    .collect(toList());

            messageListener.runnerName = method.runner().map(ResolvedTypeName::simpleName).orElse(null);

            Optional<ResolvedType> returnType = method.returnType();
            if(returnType.isPresent()
                    && !returnType.get().isPrimitive()) {
                messageListener.productionType = productionType(returnType.get());
            }

            return this;
        }

        private ProductionType productionType(ResolvedType returnType) {
            ResolvedTypeName typeName = returnType.genericTypeName();
            if(typeName.instanceOf(Collection.class.getCanonicalName())) {
                return ProductionType.SEVERAL;
            } else if(typeName.instanceOf(Optional.class.getCanonicalName())) {
                return ProductionType.OPTIONAL;
            } else {
                return ProductionType.SINGLE;
            }
        }

        public Builder withMethodName(String methodName) {
            messageListener.methodName = methodName;
            return this;
        }

        public Builder withConsumedMessage(Message consumedMessage) {
            messageListener.consumedMessage = consumedMessage;
            return this;
        }

        public Builder withProductionType(Optional<ProductionType> productionType) {
            messageListener.productionType = productionType.orElse(null);
            return this;
        }

        public Builder withRunnerName(Optional<String> runnerName) {
            messageListener.runnerName = runnerName.orElse(null);
            return this;
        }

        public Builder withRunnerClass(Optional<String> runnerClass) {
            messageListener.runnerClass = runnerClass.orElse(null);
            return this;
        }

        public Builder withProducedEvent(ProducedEvent producedEvent) {
            messageListener.producedEvents.add(producedEvent);
            return this;
        }

        public Builder withProducedEvents(List<ProducedEvent> producedEvents) {
            messageListener.producedEvents.addAll(producedEvents);
            return this;
        }

        public Builder withConsumesFromExternal(Optional<String> consumesFromExternal) {
            messageListener.consumesFromExternal = consumesFromExternal.orElse(null);
            return this;
        }

        public Builder withProcessName(String processName) {
            processNames.add(processName);
            return this;
        }

        private List<String> processNames = new ArrayList<>();

        public Builder withProcessNames(List<String> processNames) {
            this.processNames.addAll(processNames);
            return this;
        }

        public Builder withSource(Source source) {
            messageListener.source = source;
            return this;
        }
    }

    private MessageListener() {

    }

    @Override
    public boolean equals(Object obj) {
        return referenceEquals(this, obj).orElse(other -> new EqualsBuilder()
                .append(consumedMessage, other.consumedMessage)
                .append(consumesFromExternal, other.consumesFromExternal)
                .append(container, other.container)
                .append(methodName, other.methodName)
                .append(processNames, other.processNames)
                .append(producedEvents, other.producedEvents)
                .append(productionType, other.productionType)
                .append(runnerClass, other.runnerClass)
                .append(source, other.source)
                .build());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(consumedMessage)
                .append(consumesFromExternal)
                .append(container)
                .append(methodName)
                .append(processNames)
                .append(producedEvents)
                .append(productionType)
                .append(runnerClass)
                .append(source)
                .build();
    }
}
