package poussecafe.processing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerConsumptionReport;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.environment.MessageListenerType;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OriginalAndMarshaledMessage;

public class MessageListenerGroup {

    public static class Builder {

        private MessageListenerGroup group = new MessageListenerGroup();

        public Builder message(OriginalAndMarshaledMessage message) {
            group.message = message;
            return this;
        }

        public Builder listeners(List<MessageListener> listeners) {
            group.listeners = listeners;
            return this;
        }

        public Builder messageConsumptionHandler(MessageConsumptionHandler messageConsumptionHandler) {
            group.messageConsumptionHandler = messageConsumptionHandler;
            return this;
        }

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            group.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        public Builder failFast(boolean failFast) {
            group.failFast = failFast;
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

        public MessageListenerGroup build() {
            Objects.requireNonNull(group.message);
            Objects.requireNonNull(group.messageConsumptionHandler);
            Objects.requireNonNull(group.applicationPerformanceMonitoring);
            Objects.requireNonNull(group.aggregateRootClass);
            Objects.requireNonNull(group.logger);

            Objects.requireNonNull(group.listeners);
            group.listeners.sort(null);
            for(MessageListener listener : group.listeners) {
                if(listener.priority() == MessageListenerType.AGGREGATE) {
                    group.aggregateListeners.add(listener);
                } else if(listener.priority() == MessageListenerType.FACTORY) {
                    group.factoryListeners.add(listener);
                } else if(listener.priority() == MessageListenerType.REPOSITORY) {
                    group.repositoryListeners.add(listener);
                } else {
                    group.otherListeners.add(listener);
                }
            }

            return group;
        }
    }

    private OriginalAndMarshaledMessage message;

    private List<MessageListener> aggregateListeners = new ArrayList<>();

    private List<MessageListener> factoryListeners = new ArrayList<>();

    private List<MessageListener> repositoryListeners = new ArrayList<>();

    private List<MessageListener> otherListeners = new ArrayList<>();

    private List<MessageListener> listeners;

    public List<MessageListenerConsumptionReport> consumeMessageOrRetry(MessageListenerGroupConsumptionState consumptionState) {
        MessageListenerGroupConsumption groupConsumption = new MessageListenerGroupConsumption.Builder()
                .aggregateListeners(aggregateListeners)
                .applicationPerformanceMonitoring(applicationPerformanceMonitoring)
                .consumptionState(consumptionState)
                .factoryListeners(factoryListeners)
                .repositoryListeners(repositoryListeners)
                .failFast(failFast)
                .messageConsumptionHandler(messageConsumptionHandler)
                .otherListeners(otherListeners)
                .aggregateRootClass(aggregateRootClass)
                .build();
        groupConsumption.execute();
        return groupConsumption.reports();
    }

    private MessageConsumptionHandler messageConsumptionHandler;

    private boolean failFast;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

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
        return !aggregateListeners.isEmpty();
    }
}
