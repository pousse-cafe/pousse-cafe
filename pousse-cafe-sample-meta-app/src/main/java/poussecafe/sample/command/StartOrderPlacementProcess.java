package poussecafe.sample.command;

import poussecafe.messaging.Command;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.ProductKey;

public class StartOrderPlacementProcess extends Command {

    private ProductKey productKey;

    private OrderDescription description;

    public StartOrderPlacementProcess(ProductKey productKey, OrderDescription description) {
        setProductKey(productKey);
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
        return "StartOrderPlacementProcess [productKey=" + productKey + ", description=" + description + "]";
    }
}
