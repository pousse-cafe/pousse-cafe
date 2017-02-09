package poussecafe.sample.workflow;

import poussecafe.process.StateMachine;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.ProductKey;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class OrderPlacementStateMachine extends StateMachine {

    private ProductKey productKey;

    private OrderDescription orderDescription;

    public OrderPlacementStateMachine(ProductKey productKey, OrderDescription orderDescription) {
        setProductKey(productKey);
        setOrderDescription(orderDescription);
    }

    private void setProductKey(ProductKey productKey) {
        checkThat(value(productKey).notNull().because("Product key cannot be null"));
        this.productKey = productKey;
    }

    public ProductKey getProductKey() {
        return productKey;
    }

    private void setOrderDescription(OrderDescription orderDescription) {
        checkThat(value(orderDescription).notNull().because("Order description cannot be null"));
        this.orderDescription = orderDescription;
    }

    public OrderDescription getOrderDescription() {
        return orderDescription;
    }

    @Override
    protected WaitOrderPlaced initialState() {
        return new WaitOrderPlaced();
    }

}
