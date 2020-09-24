package poussecafe.processing;

import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.environment.AggregateUpdateMessageConsumer;
import poussecafe.environment.MessageListener;
import poussecafe.environment.MessageListenerConsumptionReport;
import poussecafe.environment.MessageListenerGroupConsumptionState;
import poussecafe.exception.RetryOperationException;
import poussecafe.exception.SameOperationException;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.OptimisticLockingException;
import poussecafe.runtime.OptimisticLockingExceptionHandlingResult;
import poussecafe.runtime.OriginalAndMarshaledMessage;
import poussecafe.util.MethodInvokerException;

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
        } catch (SameOperationException e) {
            ignore(e);
        } catch (RetryOperationException e) {
            retry(e);
        } catch (OptimisticLockingException e) {
            handleOptimisticLockingException(e);
        } catch (MethodInvokerException e) {
            fail(e.getCause());
        } catch (Exception e) {
            fail(e);
        }
    }

    private Optional<Object> toUpdateId = Optional.empty();

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
        logger.error("      Failure of {} with {}", listener, messageClassName, e);
        messageConsumptionHandler.handleFailure(consumptionState.consumptionId(), receivedMessage, listener, e);
    }

    private MessageListenerConsumptionReport messageConsumptionReport;

    public MessageListenerConsumptionReport messageConsumptionReport() {
        return messageConsumptionReport;
    }
}
