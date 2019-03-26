package poussecafe.shop.command;

import poussecafe.shop.domain.OrderDescription;
import poussecafe.shop.domain.ProductKey;

public class PlaceOrder {

    private ProductKey productKey;

    private OrderDescription description;

    public PlaceOrder(ProductKey productKey, OrderDescription description) {
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
        return "PlaceOrder [productKey=" + productKey + ", description=" + description + "]";
    }

}
