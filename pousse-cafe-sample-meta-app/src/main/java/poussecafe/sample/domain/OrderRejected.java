package poussecafe.sample.domain;

import poussecafe.domain.DomainEvent;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class OrderRejected extends DomainEvent {

    private ProductKey key;

    private OrderDescription description;

    public OrderRejected(ProductKey key, OrderDescription description) {
        setKey(key);
        setDescription(description);
    }

    public ProductKey getProductKey() {
        return key;
    }

    private void setKey(ProductKey key) {
        checkThat(value(key).notNull().because("Product key cannot be null"));
        this.key = key;
    }

    public OrderDescription getDescription() {
        return description;
    }

    private void setDescription(OrderDescription description) {
        checkThat(value(description).notNull().because("Order description cannot be null"));
        this.description = description;
    }

}
