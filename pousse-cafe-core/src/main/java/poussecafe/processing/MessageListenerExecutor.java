package poussecafe.processing;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.domain.DomainEvent;
import poussecafe.environment.AggregateUpdateMessageConsumer;
import poussecafe.environment.ExpectedEvent;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerConsumptionReport;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.exception.SameOperationException;
import poussecafe.runtime.FailFastException;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OptimisticLockingException;
import poussecafe.runtime.OptimisticLockingExceptionHandlingResult;
import poussecafe.runtime.OriginalAndMarshaledMessage;
import poussecafe.util.MethodInvokerException;

import static java.util.stream.Collectors.toSet;

class MessageListenerExecutor {

    static class Builder {

        private MessageListenerExecutor executor = new MessageListenerExecutor();

        public Builder consumptionState(MessageListenerGroupConsumptionState consumptionState) {
            executor.consumptionState = consumptionState;
            return this;
        }

        public Builder listener(MessageListener listener) {
            executor.listener = listener;
            return this;
        }

        public Builder messageConsumptionHandler(MessageConsumptionHandler messageConsumptionHandler) {
            executor.messageConsumptionHandler = messageConsumptionHandler;
            return this;
        }

        public Builder failFast(boolean failFast) {
            executor.failFast = failFast;
            return this;
        }

        public Builder logger(Logger logger) {
            executor.logger = logger;
            return this;
        }

        public Builder toUpdateId(Optional<Object> toUpdateId) {
            executor.toUpdateId = toUpdateId;
            return this;
        }

        public MessageListenerExecutor build() {
            Objects.requireNonNull(executor.consumptionState);
            Objects.requireNonNull(executor.listener);
            Objects.requireNonNull(executor.messageConsumptionHandler);
            Objects.requireNonNull(executor.logger);
            Objects.requireNonNull(executor.toUpdateId);
            return executor;
        }
    }

    private MessageListenerExecutor() {

    }

    private MessageListenerGroupConsumptionState consumptionState;

    private MessageListener listener;

    public MessageListener listener() {
        return listener;
    }

    private MessageConsumptionHandler messageConsumptionHandler;

    private boolean failFast;

    public void executeListener() {
        OriginalAndMarshaledMessage receivedMessage = consumptionState.message();
        String messageClassName = receivedMessage.original().getClass().getName();
        if(consumptionState.isFirstConsumption() && logger.isDebugEnabled()) {
            logger.debug("    {} consumes {}", listener.shortId(), messageClassName);
        } else if(!consumptionState.isFirstConsumption() && logger.isInfoEnabled()) {
            logger.info("    {} retries {}", listener.shortId(), messageClassName);
        }

        try {
            if(toUpdateId.isPresent()) {
                AggregateUpdateMessageConsumer consumer = (AggregateUpdateMessageConsumer) listener.consumer();
                messageConsumptionReport = consumer.consume(consumptionState, toUpdateId.get());
            } else {
                messageConsumptionReport = listener.consumer().consume(consumptionState);
            }
            if(listener.withExpectedEvents()) {
                checkProducedEvents();
            }
        } catch (SameOperationException e) {
            ignore(e);
        } catch (OptimisticLockingException e) {
            handleOptimisticLockingException(e);
        } catch (MethodInvokerException e) {
            fail(e.getCause());
        } catch (Exception e) {
            fail(e);
        }
    }

    private Optional<Object> toUpdateId = Optional.empty();

    private void checkProducedEvents() {
        List<ExpectedEvent> expectedEvents = listener.expectedEvents();
        Map<Object, List<DomainEvent>> producedEventsByAggregate = messageConsumptionReport.producedEventsByAggregate();
        for(Entry<Object, List<DomainEvent>> entry : producedEventsByAggregate.entrySet()) {
            Set<ExpectedEvent> requiredEvents = expectedEvents.stream()
                    .filter(ExpectedEvent::required)
                    .collect(toSet());
            for(DomainEvent producedEvent : entry.getValue()) {
                Optional<ExpectedEvent> match = expectedEvents.stream().filter(expectedEvent -> expectedEvent.matches(producedEvent)).findFirst();
                if(match.isPresent()) {
                    requiredEvents.remove(match.get());
                } else {
                    throw new IllegalStateException("Produced event " + producedEvent + " does not match any expected event of listener " + listener.shortId() + " with aggregate " + entry.getKey());
                }
            }
            if(!requiredEvents.isEmpty()) {
                throw new IllegalStateException("Some required events where not produced by listener " + listener.shortId() + " with aggregate " + entry.getKey());
            }
        }
    }

    private void ignore(Throwable e) {
        logger.warn("       Ignoring consumption error", e);
        messageConsumptionReport = new MessageListenerConsumptionReport.Builder(listener.shortId())
                .skipped(true)
                .build();
    }

    private void handleOptimisticLockingException(OptimisticLockingException e) {
        OptimisticLockingExceptionHandlingResult result = messageConsumptionHandler.handleOptimisticLockingException(consumptionState.message());
        if(result == OptimisticLockingExceptionHandlingResult.RETRY) {
            retry(e);
        } else if(result == OptimisticLockingExceptionHandlingResult.FAIL) {
            fail(e);
        } else if(result == OptimisticLockingExceptionHandlingResult.IGNORE) {
            ignore(e);
        } else {
            throw new UnsupportedOperationException("Unexpected OptimisticLockingExceptionHandlingResult " + result);
        }
    }

    private void retry(Throwable e) {
        logger.warn("       Retrying following consumption error", e);
        messageConsumptionReport = new MessageListenerConsumptionReport.Builder(listener.shortId())
                .toRetry(true)
                .build();
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private void fail(Throwable e) {
        messageConsumptionReport = new MessageListenerConsumptionReport.Builder(listener.shortId())
                .failure(e)
                .build();

        OriginalAndMarshaledMessage receivedMessage = consumptionState.message();
        String messageClassName = receivedMessage.original().getClass().getName();
        if(failFast) {
            logger.error("      Failing fast on exception from listener {}", listener, e);
            throw new FailFastException("Failing fast on exception from listener {}", e);
        } else {
            logger.error("      Failure of {} with {}", listener, messageClassName, e);
            messageConsumptionHandler.handleFailure(consumptionState.consumptionId(), receivedMessage, listener, e);
        }
    }

    private MessageListenerConsumptionReport messageConsumptionReport;

    public MessageListenerConsumptionReport messageConsumptionReport() {
        return messageConsumptionReport;
    }
}
