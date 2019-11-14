package poussecafe.environment;

@FunctionalInterface
public interface MessageConsumer {

    MessageListenerConsumptionReport consume(MessageListenerGroupConsumptionState state);
}
