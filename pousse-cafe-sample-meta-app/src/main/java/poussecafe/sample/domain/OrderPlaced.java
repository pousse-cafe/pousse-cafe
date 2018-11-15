package poussecafe.sample.domain;

import poussecafe.domain.DomainEvent;

public class OrderPlaced implements DomainEvent {

    private ProductKey productKey;

    private OrderDescription description;

    public OrderPlaced(ProductKey key, OrderDescription description) {
        setProductKey(key);
        setOrderDescription(description);
    }

    public ProductKey getProductKey() {
        return productKey;
    }

    private void setProductKey(ProductKey productKey) {
        this.productKey = productKey;
    }

    public OrderDescription getOrderDescription() {
        return description;
    }

    private void setOrderDescription(OrderDescription description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "OrderPlaced [productKey=" + productKey + ", description=" + description + "]";
    }

}
