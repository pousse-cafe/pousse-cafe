package poussecafe.sample.command;

import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.ProductKey;

public class CreateOrder {

    private ProductKey productKey;

    private OrderDescription description;

    public CreateOrder(ProductKey key, OrderDescription description) {
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
        return "CreateOrder [productKey=" + productKey + ", description=" + description + "]";
    }
}
