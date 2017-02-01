package poussecafe.sample.domain;

public class OrderDescription {

    public String reference;

    public int units;

    @Override
    public String toString() {
        return "OrderDescription [reference=" + reference + ", units=" + units + "]";
    }
}
