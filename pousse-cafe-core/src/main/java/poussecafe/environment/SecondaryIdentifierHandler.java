package poussecafe.environment;

import static java.util.Objects.requireNonNull;

public class SecondaryIdentifierHandler<K, A> {

    public AggregateRetriever<K, A> aggregateRetriever() {
        return aggregateRetriever;
    }

    private AggregateRetriever<K, A> aggregateRetriever;

    public IdentifierExtractor<A, K> identifierExtractor() {
        return identifierExtractor;
    }

    private IdentifierExtractor<A, K> identifierExtractor;

    public static class Builder<K, A> {

        private SecondaryIdentifierHandler<K, A> handler = new SecondaryIdentifierHandler<>();

        public SecondaryIdentifierHandler<K, A> build() {
            requireNonNull(handler.aggregateRetriever);
            requireNonNull(handler.identifierExtractor);
            return handler;
        }

        public Builder<K, A> aggregateRetriever(AggregateRetriever<K, A> aggregateRetriever) {
            handler.aggregateRetriever = aggregateRetriever;
            return this;
        }

        public Builder<K, A> identifierExtractor(IdentifierExtractor<A, K> identifierExtractor) {
            handler.identifierExtractor = identifierExtractor;
            return this;
        }
    }

    private SecondaryIdentifierHandler() {

    }
}
