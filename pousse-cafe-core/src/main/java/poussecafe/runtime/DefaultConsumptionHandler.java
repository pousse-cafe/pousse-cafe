package poussecafe.runtime;

import poussecafe.environment.MessageListener;

public class DefaultConsumptionHandler implements MessageConsumptionHandler {

    @Override
    public void handleSuccess(String consumptionId, OriginalAndMarshaledMessage receivedMessage, MessageListener listener) {
        // By default, do nothing.
    }

    @Override
    public void handleFailure(String consumptionId, OriginalAndMarshaledMessage receivedMessage, MessageListener listener, Exception e) {
        // By default, do nothing.
    }
}
