package poussecafe.processing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.domain.AggregateRoot;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.SecondaryIdentifierHandler;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerConsumptionReport;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.environment.MessageListenerType;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class MessageListenersGroup {

    public static class Builder {

        private MessageListenersGroup group = new MessageListenersGroup();

        public Builder message(OriginalAndMarshaledMessage message) {
            group.message = message;
            return this;
        }

        public Builder listeners(List<MessageListener> listeners) {
            group.listeners = listeners;
            return this;
        }

        public Builder messageConsumptionContext(MessageConsumptionContext messageConsumptionContext) {
            group.messageConsumptionContext = messageConsumptionContext;
            return this;
        }

        @SuppressWarnings("rawtypes")
        public Builder aggregateRootClass(Optional<Class> aggregateRootClass) {
            group.aggregateRootClass = aggregateRootClass;
            return this;
        }

        public Builder logger(Logger logger) {
            group.logger = logger;
            return this;
        }

        public MessageListenersGroup build() {
            Objects.requireNonNull(group.message);
            Objects.requireNonNull(group.messageConsumptionContext);
            Objects.requireNonNull(group.aggregateRootClass);
            Objects.requireNonNull(group.logger);

            Objects.requireNonNull(group.listeners);
            group.listeners.sort(null);
            for(MessageListener listener : group.listeners) {
                if(listener.type() == MessageListenerType.AGGREGATE) {
                    group.aggregateListener = ifEmptyOrElseThrow(listener, group.aggregateListener, "Aggregate");
                } else if(listener.type() == MessageListenerType.FACTORY) {
                    group.factoryListener = ifEmptyOrElseThrow(listener, group.factoryListener, "Factory");
                } else if(listener.type() == MessageListenerType.REPOSITORY) {
                    group.repositoryListener = ifEmptyOrElseThrow(listener, group.repositoryListener, "Repository");
                } else {
                    group.otherListeners.add(listener);
                }
            }

            return group;
        }

        private Optional<MessageListener> ifEmptyOrElseThrow(MessageListener listener, Optional<MessageListener> optional, String componentName) {
            if(group.aggregateRootClass.isEmpty()) {
                throw new IllegalStateException(componentName + " listeners must be associated with an aggregate");
            }
            if(optional.isEmpty()) {
                return Optional.of(listener);
            } else {
                throw new IllegalStateException("There must be 1 or 0 " + componentName.toLowerCase() + " listener per group");
            }
        }
    }

    private OriginalAndMarshaledMessage message;

    public OriginalAndMarshaledMessage message() {
        return message;
    }

    private Optional<MessageListener> aggregateListener = Optional.empty();

    private Optional<MessageListener> factoryListener = Optional.empty();

    private Optional<MessageListener> repositoryListener = Optional.empty();

    private List<MessageListener> otherListeners = new ArrayList<>();

    private List<MessageListener> listeners;

    public List<MessageListenerConsumptionReport> consumeMessageOrRetry(MessageListenerGroupConsumptionState consumptionState) {
        MessageListenerGroupConsumption groupConsumption = new MessageListenerGroupConsumption.Builder()
                .aggregateListener(aggregateListener)
                .messageConsumptionContext(messageConsumptionContext)
                .consumptionState(consumptionState)
                .factoryListener(factoryListener)
                .repositoryListener(repositoryListener)
                .otherListeners(otherListeners)
                .aggregateRootClass(aggregateRootClass)
                .build();
        groupConsumption.execute();
        return groupConsumption.reports();
    }

    private MessageConsumptionContext messageConsumptionContext;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    public Optional<Class> aggregateRootClass() {
        return aggregateRootClass;
    }

    @SuppressWarnings("rawtypes")
    private Optional<Class> aggregateRootClass = Optional.empty();

    public List<MessageListener> listeners() {
        return Collections.unmodifiableList(listeners);
    }

    public boolean hasUpdates() {
        return aggregateListener.isPresent();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object aggregateId(AggregateRoot aggregate) {
        Optional<SecondaryIdentifierHandler> identifierHandler = runner().map(AggregateMessageListenerRunner::secondaryIdentifierHandler);
        if(identifierHandler.isEmpty()
                || identifierHandler.get() == null) {
            return aggregate.attributes().identifier().value();
        } else {
            return identifierHandler.get().identifierExtractor().extractFrom(aggregate);
        }
    }

    @SuppressWarnings("rawtypes")
    public Optional<AggregateMessageListenerRunner> runner() {
        if(aggregateListener.isPresent()) {
            return aggregateListener.get().runner();
        } else {
            return Optional.empty();
        }
    }
}
