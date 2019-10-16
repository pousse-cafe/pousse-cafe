package poussecafe.runtime;

import poussecafe.environment.MessageListener;

public class DefaultConsumptionHandler implements MessageConsumptionHandler {

    @Override
    public void handleSuccess(String consumptionId, OriginalAndMarshaledMessage receivedMessage, MessageListener listener) {
        // By default, do nothing.
    }

    @Override
    public void handleFailure(String consumptionId, OriginalAndMarshaledMessage receivedMessage, MessageListener listener, Throwable e) {
        // By default, do nothing.
    }

    @Override
    public OptimisticLockingExceptionHandlingResult handleOptimisticLockingException(OriginalAndMarshaledMessage original) {
        return OptimisticLockingExceptionHandlingResult.RETRY;
    }
}
