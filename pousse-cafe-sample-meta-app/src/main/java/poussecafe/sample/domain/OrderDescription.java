package poussecafe.sample.domain;

public class OrderDescription {

    public CustomerKey customerKey;

    public String reference;

    public int units;

    @Override
    public String toString() {
        return "OrderDescription [customerKey=" + customerKey + ", reference=" + reference + ", units=" + units + "]";
    }
}
