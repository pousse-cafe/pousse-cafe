package poussecafe.storable;

@FunctionalInterface
public interface AddOperator<N extends Number> {

    N add(N n1, N n2);
}
