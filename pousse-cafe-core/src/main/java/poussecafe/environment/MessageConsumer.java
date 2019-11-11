package poussecafe.environment;

@FunctionalInterface
public interface MessageConsumer {

    MessageConsumptionReport consume(MessageListenerGroupConsumptionState state);
}
