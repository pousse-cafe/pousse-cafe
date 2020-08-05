package poussecafe.environment;

@FunctionalInterface
public interface IdentifierExtractor<A, K> {

    K extractFrom(A aggregate);
}
