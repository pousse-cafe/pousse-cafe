package poussecafe.runtime;

import poussecafe.environment.MessageListener;

public interface MessageConsumptionHandler {

    void handleSuccess(String consumptionId, OriginalAndMarshaledMessage receivedMessage, MessageListener listener);

    void handleFailure(String consumptionId, OriginalAndMarshaledMessage receivedMessage, MessageListener listener, Throwable e);

    OptimisticLockingExceptionHandlingResult handleOptimisticLockingException(OriginalAndMarshaledMessage original);
}
