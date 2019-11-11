package poussecafe.environment;

import poussecafe.messaging.Message;

@FunctionalInterface
public interface MessageConsumer {

    MessageConsumptionReport consume(Message message);
}
