package poussecafe.attribute;

public interface NumberAttribute<N extends Number> extends Attribute<N> {

    void add(N term);
}
