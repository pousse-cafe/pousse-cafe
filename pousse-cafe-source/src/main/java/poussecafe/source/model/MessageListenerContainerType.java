package poussecafe.source.model;

public enum MessageListenerContainerType {
    INNER_FACTORY,
    INNER_ROOT,
    INNER_REPOSITORY,
    STANDALONE_FACTORY,
    STANDALONE_ROOT,
    STANDALONE_REPOSITORY,
    OTHER;

    public boolean isFactory() {
        return this == INNER_FACTORY || this == STANDALONE_FACTORY;
    }

    public boolean isRoot() {
        return this == INNER_ROOT || this == STANDALONE_ROOT;
    }

    public boolean isRepository() {
        return this == INNER_REPOSITORY || this == STANDALONE_REPOSITORY;
    }
}
