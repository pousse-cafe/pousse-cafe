package poussecafe.processing;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

class MessageListenerExecutor {

    static class Builder {

        private MessageListenerExecutor executor = new MessageListenerExecutor();

        public Builder consumptionId(String consumptionId) {
            executor.consumptionId = consumptionId;
            return this;
        }

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

        public MessageListenerExecutor build() {
            Objects.requireNonNull(executor.consumptionId);
            Objects.requireNonNull(executor.consumptionState);
            Objects.requireNonNull(executor.listener);
            Objects.requireNonNull(executor.messageConsumptionHandler);
            return executor;
        }
    }

    private MessageListenerExecutor() {

    }

    private String consumptionId;

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
        if(logger.isDebugEnabled()) {
            logger.debug("    {} consumes {}", listener.shortId(), messageClassName);
        }

        try {
            messageConsumptionReport = listener.consumer().consume(consumptionState);
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

    private void ignore(Throwable e) {
        logger.warn("       Ignoring consumption error", e);
        messageConsumptionReport = new MessageListenerConsumptionReport.Builder()
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
        messageConsumptionReport = new MessageListenerConsumptionReport.Builder()
                .toRetry(true)
                .build();
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private void fail(Throwable e) {
        messageConsumptionReport = new MessageListenerConsumptionReport.Builder()
                .failure(e)
                .build();

        OriginalAndMarshaledMessage receivedMessage = consumptionState.message();
        String messageClassName = receivedMessage.original().getClass().getName();
        if(failFast) {
            logger.error("      Failing fast on exception from listener {}", listener, e);
            throw new FailFastException("Failing fast on exception from listener {}", e);
        } else {
            logger.error("      Failure of {} with {}", listener, messageClassName, e);
            messageConsumptionHandler.handleFailure(consumptionId, receivedMessage, listener, e);
        }
    }

    private MessageListenerConsumptionReport messageConsumptionReport;

    public MessageListenerConsumptionReport messageConsumptionReport() {
        return messageConsumptionReport;
    }
}
