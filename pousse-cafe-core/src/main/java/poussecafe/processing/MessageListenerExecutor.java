package poussecafe.processing;

import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.environment.MessageListener;
import poussecafe.exception.SameOperationException;
import poussecafe.runtime.FailFastException;
import poussecafe.runtime.MessageConsumptionHandler;
import poussecafe.runtime.MessageListenerExecutionStatus;
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

        public Builder receivedMessage(OriginalAndMarshaledMessage receivedMessage) {
            executor.receivedMessage = receivedMessage;
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
            Objects.requireNonNull(executor.receivedMessage);
            Objects.requireNonNull(executor.listener);
            Objects.requireNonNull(executor.messageConsumptionHandler);
            return executor;
        }
    }

    private MessageListenerExecutor() {

    }

    private String consumptionId;

    private OriginalAndMarshaledMessage receivedMessage;

    private MessageListener listener;

    public MessageListener listener() {
        return listener;
    }

    private MessageConsumptionHandler messageConsumptionHandler;

    private boolean failFast;

    public void executeListener() {
        String messageClassName = receivedMessage.original().getClass().getName();
        if(logger.isDebugEnabled()) {
            logger.debug("    {} consumes {}", listener.shortId(), messageClassName);
        }

        try {
            listener.consumer().accept(receivedMessage.original());
            succeed();
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

    private void succeed() {
        String messageClassName = receivedMessage.original().getClass().getName();
        logger.debug("      Success of {} with {}", listener, messageClassName);

        messageConsumptionHandler.handleSuccess(consumptionId, receivedMessage, listener);
        status = MessageListenerExecutionStatus.SUCCESS;
    }

    private void ignore(Throwable e) {
        logger.warn("       Ignoring consumption error", e);
        status = MessageListenerExecutionStatus.IGNORED;
    }

    private void handleOptimisticLockingException(OptimisticLockingException e) {
        OptimisticLockingExceptionHandlingResult result = messageConsumptionHandler.handleOptimisticLockingException(receivedMessage);
        if(result == OptimisticLockingExceptionHandlingResult.RETRY) {
            retry(e);
        } else if(result == OptimisticLockingExceptionHandlingResult.FAIL) {
            fail(e);
        } else if(result == OptimisticLockingExceptionHandlingResult.IGNORE) {
            ignore(e);
        }
    }

    private void retry(Throwable e) {
        logger.warn("       Retrying following consumption error", e);
        status = MessageListenerExecutionStatus.EXPECTING_RETRY;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private void fail(Throwable e) {
        status = MessageListenerExecutionStatus.FAILED;
        executionError = e;

        String messageClassName = receivedMessage.original().getClass().getName();
        if(failFast) {
            logger.error("      Failing fast on exception from listener {}", listener, e);
            throw new FailFastException("Failing fast on exception from listener {}", e);
        } else {
            logger.error("      Failure of {} with {}", listener, messageClassName, e);
            messageConsumptionHandler.handleFailure(consumptionId, receivedMessage, listener, e);
        }
    }

    private MessageListenerExecutionStatus status = MessageListenerExecutionStatus.NOT_EXECUTED;

    public MessageListenerExecutionStatus status() {
        return status;
    }

    private Throwable executionError;

    public Optional<Throwable> executionError() {
        return Optional.ofNullable(executionError);
    }
}
