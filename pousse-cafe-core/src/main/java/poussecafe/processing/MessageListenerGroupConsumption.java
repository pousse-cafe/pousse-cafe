package poussecafe.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.apm.ApmTransaction;
import poussecafe.apm.ApmTransactionLabels;
import poussecafe.apm.ApmTransactionResults;
import poussecafe.environment.AggregateMessageListenerRunner;
import poussecafe.environment.AggregateUpdateMessageConsumer;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerConsumptionReport;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.environment.TargetAggregates;
import poussecafe.messaging.Message;
import poussecafe.util.MethodInvokerException;

import static java.util.stream.Collectors.toList;

public class MessageListenerGroupConsumption {

    public static class Builder {

        private MessageListenerGroupConsumption consumption = new MessageListenerGroupConsumption();

        public Builder consumptionState(MessageListenerGroupConsumptionState consumptionState) {
            consumption.consumptionState = consumptionState;
            return this;
        }

        public Builder aggregateListener(Optional<MessageListener> aggregateListener) {
            consumption.aggregateListener = aggregateListener;
            return this;
        }

        public Builder factoryListener(Optional<MessageListener> factoryListener) {
            consumption.factoryListener = factoryListener;
            return this;
        }

        public Builder repositoryListener(Optional<MessageListener> repositoryListener) {
            consumption.repositoryListener = repositoryListener;
            return this;
        }

        public Builder otherListeners(List<MessageListener> otherListeners) {
            consumption.otherListeners = otherListeners;
            return this;
        }

        public Builder messageConsumptionContext(MessageConsumptionContext messageConsumptionContext) {
            consumption.messageConsumptionContext = messageConsumptionContext;
            return this;
        }

        @SuppressWarnings("rawtypes")
        public Builder aggregateRootClass(Optional<Class> aggregateRootClass) {
            consumption.aggregateRootClass = aggregateRootClass;
            return this;
        }

        public MessageListenerGroupConsumption build() {
            Objects.requireNonNull(consumption.consumptionState);
            Objects.requireNonNull(consumption.aggregateListener);
            Objects.requireNonNull(consumption.factoryListener);
            Objects.requireNonNull(consumption.repositoryListener);
            Objects.requireNonNull(consumption.otherListeners);
            Objects.requireNonNull(consumption.messageConsumptionContext);
            Objects.requireNonNull(consumption.aggregateRootClass);
            return consumption;
        }
    }

    private MessageListenerGroupConsumption() {

    }

    private MessageListenerGroupConsumptionState consumptionState;

    private Optional<MessageListener> aggregateListener;

    private Optional<MessageListener> factoryListener;

    private Optional<MessageListener> repositoryListener;

    private List<MessageListener> otherListeners;

    private MessageConsumptionContext messageConsumptionContext;

    public void execute() {
        executeRepositoryListeners();
        executeUpdates();
        executeCreations();
        planUpdatesRetryFromMissingCreations();
        executeOtherListeners();
    }

    private void executeRepositoryListeners() {
        repositoryListener.ifPresent(this::executeSimpleListener);
    }

    private void executeSimpleListeners(Collection<MessageListener> listeners) {
        for(MessageListener listener : listeners) {
            executeSimpleListener(listener);
        }
    }

    private void executeSimpleListener(MessageListener listener) {
        MessageListenerExecutor executor = new MessageListenerExecutor.Builder()
                .listener(listener)
                .messageConsumptionHandler(messageConsumptionContext.messageConsumptionHandler())
                .consumptionState(consumptionState)
                .logger(logger)
                .build();
        reports.add(executeInApmTransaction(executor));
    }

    private List<MessageListenerConsumptionReport> reports = new ArrayList<>();

    public List<MessageListenerConsumptionReport> reports() {
        return Collections.unmodifiableList(reports);
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private MessageListenerConsumptionReport executeInApmTransaction(MessageListenerExecutor executor) {
        String transactionName = executor.listener().shortId();
        ApmTransaction apmTransaction = messageConsumptionContext.applicationPerformanceMonitoring()
                .startTransaction(transactionName);
        try {
            executor.executeListener();
            configureApmTransaction(executor, apmTransaction);
            return executor.messageConsumptionReport();
        } catch (Exception e) {
            logger.error("Listener failed", e);
            apmTransaction.setResult(ApmTransactionResults.FAILURE);
            captureException(apmTransaction, e);
            return new MessageListenerConsumptionReport.Builder(executor.listener().shortId())
                    .failure(e)
                    .build();
        } finally {
            apmTransaction.end();
        }
    }

    private void configureApmTransaction(MessageListenerExecutor executor, ApmTransaction apmTransaction) {
        MessageListenerConsumptionReport report = executor.messageConsumptionReport();
        if(report.isSuccess()) {
            apmTransaction.setResult(ApmTransactionResults.SUCCESS);
        } else if(report.isFailed()) {
            apmTransaction.setResult(ApmTransactionResults.FAILURE);
            if(report.failure() != null) {
                captureException(apmTransaction, report.failure());
            } else if(!report.failures().isEmpty()) {
                Iterator<Throwable> exceptionsIterator = report.failures().values().iterator();
                Throwable oneException = exceptionsIterator.next();
                captureException(apmTransaction, oneException);
                if(exceptionsIterator.hasNext()) {
                    logger.warn("Some exceptions where not captured by APM");
                }
            }
        } else if(report.mustRetry()) {
            apmTransaction.setResult(ApmTransactionResults.SKIP);
            apmTransaction.addLabel(ApmTransactionLabels.SKIP_REASON, "retry_later");
        } else if(report.isSkipped()) {
            apmTransaction.setResult(ApmTransactionResults.SKIP);
            apmTransaction.addLabel(ApmTransactionLabels.SKIP_REASON, "already_executed");
        } else {
            throw new IllegalArgumentException("Unsupported consumption report");
        }
    }

    private void captureException(ApmTransaction apmTransaction, Throwable e) {
        if(e instanceof MethodInvokerException) {
            apmTransaction.captureException(e.getCause());
        } else {
            apmTransaction.captureException(e);
        }
    }

    private void executeUpdates() {
        planUpdates();
        logMissingUpdates();
        executeUpdateListeners();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void planUpdates() {
        if(aggregateListener.isPresent()) {
            MessageListener listener = aggregateListener.get();
            Optional<TargetAggregates> targetAggregates = safelyGetTargetAggregates(listener);
            if(targetAggregates.isPresent()) {
                for(Object aggregateId : targetAggregates.get().toUpdate()) {
                    updates.put(aggregateId, listener);
                }
                toCreate.addAll(targetAggregates.get().toCreate());
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Optional<TargetAggregates> safelyGetTargetAggregates(MessageListener listener) {
        AggregateUpdateMessageConsumer consumer = (AggregateUpdateMessageConsumer) listener.consumer();
        AggregateMessageListenerRunner runner = consumer.runner();
        Message original = consumptionState.message().original();
        try {
            return Optional.of(runner.targetAggregates(original));
        } catch (Exception e) {
            logger.error("Runner of listener {} failed", listener.shortId(), e);
            return Optional.empty();
        }
    }

    private Map<Object, MessageListener> updates = new HashMap<>();

    private Set<Object> toCreate = new HashSet<>();

    private void logMissingUpdates() {
        Set<Object> missingAggregates = new HashSet<>(consumptionState.toUpdate());
        missingAggregates.removeAll(updates.keySet());
        missingAggregates.removeAll(toCreate);
        if(!missingAggregates.isEmpty()
                && logger.isErrorEnabled()) {
            List<String> stringIds = missingAggregates.stream().map(Object::toString).collect(toList());
            logger.error("Missing expected aggregates to update: {}", String.join(", ", stringIds));
        }
    }

    private void executeUpdateListeners() {
        for(Entry<Object, MessageListener> entry : updates.entrySet()) {
            Object aggregateId = entry.getKey();
            MessageListenerExecutor executor = new MessageListenerExecutor.Builder()
                    .listener(entry.getValue())
                    .messageConsumptionHandler(messageConsumptionContext.messageConsumptionHandler())
                    .consumptionState(consumptionState)
                    .logger(logger)
                    .toUpdateId(Optional.of(aggregateId))
                    .build();
            MessageListenerConsumptionReport report = executeInApmTransaction(executor);
            updatesReports.add(report);
            reports.add(report);
        }
    }

    private List<MessageListenerConsumptionReport> updatesReports = new ArrayList<>();

    private void executeCreations() {
        if(factoryListener.isPresent()) {
            MessageListener listener = factoryListener.get();
            MessageListenerExecutor executor = new MessageListenerExecutor.Builder()
                    .listener(listener)
                    .messageConsumptionHandler(messageConsumptionContext.messageConsumptionHandler())
                    .consumptionState(consumptionState)
                    .logger(logger)
                    .build();
            MessageListenerConsumptionReport report = executeInApmTransaction(executor);
            if(report.failure() != null) {
                unidentifiedCreationFailures.add(report.failure());
            }
            created.addAll(report.successfulAggregatesIds());
            failedCreations.addAll(report.failures().keySet());
            toUpdateFollowingCreation.addAll(report.aggregateIdsToRetry());
            reports.add(report);
        }
    }

    private Set<Object> created = new HashSet<>();

    private Set<Object> failedCreations = new HashSet<>();

    private List<Throwable> unidentifiedCreationFailures = new ArrayList<>();

    private Set<Object> toUpdateFollowingCreation = new HashSet<>();

    private void planUpdatesRetryFromMissingCreations() {
        if(unidentifiedCreationFailures.isEmpty()) {
            Set<Object> missingCreations = new HashSet<>();
            missingCreations.addAll(toCreate);
            missingCreations.removeAll(created);
            missingCreations.removeAll(failedCreations);
            missingCreations.removeAll(toUpdateFollowingCreation);
            for(MessageListenerConsumptionReport report : updatesReports) {
                missingCreations.removeAll(report.aggregateIdsToRetry());
            }
            if(!missingCreations.isEmpty()) {
                MessageListenerConsumptionReport.Builder missingCreationsReport = new MessageListenerConsumptionReport
                        .Builder("groupMissingCreations");
                missingCreationsReport.aggregateType(aggregateRootClass.orElseThrow());
                for(Object id : missingCreations) {
                    missingCreationsReport.aggregateId(id);
                    missingCreationsReport.aggregateIdToRetry(id);
                }
                reports.add(missingCreationsReport.build());
            }
        } else {
            logger.error("{} unidentified creation failure(s), skipping group retry for missing creations", unidentifiedCreationFailures.size());
        }
    }

    private void executeOtherListeners() {
        executeSimpleListeners(otherListeners);
    }

    @SuppressWarnings("rawtypes")
    private Optional<Class> aggregateRootClass = Optional.empty();

    public boolean hasUpdates() {
        return aggregateListener.isPresent();
    }
}
