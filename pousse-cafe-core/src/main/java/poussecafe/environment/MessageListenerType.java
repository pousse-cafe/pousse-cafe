package poussecafe.environment;

// WARNING: order of values is used to order message listeners
public enum MessageListenerType {
    REPOSITORY,
    AGGREGATE,
    FACTORY,
    DOMAIN_PROCESS,
    CUSTOM
}
