package poussecafe.environment;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import poussecafe.apm.ApmSpan;
import poussecafe.apm.ApplicationPerformanceMonitoring;
import poussecafe.domain.AggregateRepository;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainEvent;
import poussecafe.exception.NotFoundException;
import poussecafe.exception.RetryOperationException;
import poussecafe.exception.SameOperationException;
import poussecafe.messaging.Message;
import poussecafe.runtime.TransactionRunnerLocator;
import poussecafe.storage.TransactionRunner;
import poussecafe.util.MethodInvoker;
import poussecafe.util.MethodInvokerException;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AggregateUpdateMessageConsumer implements MessageConsumer {

    public static class Builder {

        private AggregateUpdateMessageConsumer consumer = new AggregateUpdateMessageConsumer();

        public Builder listenerId(String listenerId) {
            consumer.listenerId = listenerId;
            return this;
        }

        public Builder method(Method method) {
            consumer.method = method;
            return this;
        }

        public Builder transactionRunnerLocator(TransactionRunnerLocator transactionRunnerLocator) {
            consumer.transactionRunnerLocator = transactionRunnerLocator;
            return this;
        }

        public Builder aggregateServices(AggregateServices aggregateServices) {
            consumer.aggregateServices = aggregateServices;
            return this;
        }

        public Builder runner(AggregateMessageListenerRunner runner) {
            consumer.runner = runner;
            return this;
        }

        public Builder applicationPerformanceMonitoring(ApplicationPerformanceMonitoring applicationPerformanceMonitoring) {
            consumer.applicationPerformanceMonitoring = applicationPerformanceMonitoring;
            return this;
        }

        public Builder expectedEvents(List<ExpectedEvent> expectedEvents) {
            consumer.expectedEvents = expectedEvents;
            return this;
        }

        public Builder hasExpectedEvents(boolean hasExpectedEvents) {
            consumer.hasExpectedEvents = hasExpectedEvents;
            return this;
        }

        public AggregateUpdateMessageConsumer build() {
            Objects.requireNonNull(consumer.listenerId);
            Objects.requireNonNull(consumer.transactionRunnerLocator);
            Objects.requireNonNull(consumer.aggregateServices);
            Objects.requireNonNull(consumer.method);
            Objects.requireNonNull(consumer.applicationPerformanceMonitoring);
            Objects.requireNonNull(consumer.expectedEvents);
            return consumer;
        }
    }

    private AggregateUpdateMessageConsumer() {

    }

    @Override
    public MessageListenerConsumptionReport consume(MessageListenerGroupConsumptionState state) {
        throw new UnsupportedOperationException();
    }

    public MessageListenerConsumptionReport consume(MessageListenerGroupConsumptionState state, Object aggregateId) {
        Message message = state.message().original();
        MessageListenerConsumptionReport.Builder reportBuilder = new MessageListenerConsumptionReport.Builder(listenerId);
        reportBuilder.logger(state.processorLogger());
        Class entityClass = aggregateServices.aggregateRootEntityClass();
        reportBuilder.aggregateType(entityClass);
        AggregateRepository repository = aggregateServices.repository();
        TransactionRunner transactionRunner = transactionRunnerLocator.locateTransactionRunner(entityClass);
        reportBuilder.runAndReport(state, aggregateId, () -> updateAggregate(message, repository, transactionRunner, aggregateId));
        return reportBuilder.build();
    }

    private String listenerId;

    private AggregateRoot updateAggregate(Message message, AggregateRepository repository, TransactionRunner transactionRunner, Object id) {
        AggregateReference reference = new AggregateReference();
        transactionRunner.runInTransaction(() -> updateInSpan(message, repository, id, reference));
        return reference.aggregate;
    }

    private class AggregateReference {

        AggregateRoot aggregate;
    }

    private void updateInSpan(Message message, AggregateRepository repository, Object id, AggregateReference reference) {
        ApmSpan span = applicationPerformanceMonitoring.currentSpan().startSpan();
        span.setName(method.getName());
        try {
            SecondaryIdentifierHandler identifierHandler = runner.secondaryIdentifierHandler();
            Optional<AggregateRoot> optionalAggregateRoot;
            if(identifierHandler == null) {
                optionalAggregateRoot = repository.getOptional(id);
            } else {
                optionalAggregateRoot = identifierHandler.aggregateRetriever().retrieve(id);
            }

            runner.validChronologyOrElseThrow(message, id, optionalAggregateRoot);

            var targetAggregateRoot = optionalAggregateRoot
                    .orElseThrow(() -> new NotFoundException("Aggregate with id " + id + " not found"));
            targetAggregateRoot.context(runner.context(message, targetAggregateRoot));

            MethodInvoker invoker = new MethodInvoker.Builder()
                    .method(method)
                    .target(targetAggregateRoot)
                    .rethrow(SameOperationException.class)
                    .rethrow(RetryOperationException.class)
                    .build();
            invoker.invoke(message);
            if(hasExpectedEvents) {
                checkProducedEvents(id, targetAggregateRoot);
            }
            repository.update(targetAggregateRoot);

            reference.aggregate = targetAggregateRoot;
        } catch(SameOperationException | RetryOperationException e) {
            throw e;
        } catch(MethodInvokerException e) {
            span.captureException(e.getCause());
            throw e;
        } catch(Exception e) {
            span.captureException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    private void checkProducedEvents(Object id, AggregateRoot root) {
        Set<ExpectedEvent> requiredEvents = expectedEvents.stream()
                .filter(ExpectedEvent::required)
                .collect(toSet());
        for(Message producedEvent : root.messageCollection().getMessages()) {
            Optional<ExpectedEvent> match = expectedEvents.stream()
                    .filter(expectedEvent -> expectedEvent.matches((DomainEvent) producedEvent))
                    .findFirst();
            if(match.isPresent()) {
                requiredEvents.remove(match.get());
            } else {
                throw new IllegalStateException("Produced event with class " + producedEvent.getClass().getCanonicalName()
                        + " does not match any expected event of listener " + listenerId + " with aggregate "
                        + id);
            }
        }
        if(!requiredEvents.isEmpty()) {
            throw new IllegalStateException("Required events where not produced by listener "
                    + listenerId + " with aggregate " + id + ": "
                    + requiredEvents.stream()
                        .map(ExpectedEvent::producedEventClass)
                        .map(Class::getCanonicalName)
                        .collect(joining(", ")));
        }
    }

    private ApplicationPerformanceMonitoring applicationPerformanceMonitoring;

    private AggregateMessageListenerRunner runner;

    public AggregateMessageListenerRunner runner() {
        return runner;
    }

    private Method method;

    private TransactionRunnerLocator transactionRunnerLocator;

    private AggregateServices aggregateServices;

    private List<ExpectedEvent> expectedEvents;

    private boolean hasExpectedEvents;
}
