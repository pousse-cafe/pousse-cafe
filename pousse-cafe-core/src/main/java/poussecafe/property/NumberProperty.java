package poussecafe.property;

public interface NumberProperty<N extends Number> extends Property<N> {

    void add(N term);
}
