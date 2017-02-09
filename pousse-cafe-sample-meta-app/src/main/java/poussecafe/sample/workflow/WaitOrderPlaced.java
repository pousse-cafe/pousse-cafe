package poussecafe.sample.workflow;

import poussecafe.process.Init;
import poussecafe.sample.command.CreateOrder;
import poussecafe.sample.command.PlaceOrder;

public class WaitOrderPlaced extends Init {

    @Override
    public void start() {
        addCommand(new PlaceOrder(stateMachine().getProductKey(), stateMachine().getOrderDescription()));
    }

    private OrderPlacementStateMachine stateMachine() {
        return (OrderPlacementStateMachine) stateMachine;
    }

    public WaitOrderCreated toWaitOrderCreated() {
        addCommand(new CreateOrder(stateMachine().getProductKey(), stateMachine().getOrderDescription()));
        return new WaitOrderCreated();
    }

    public OrderPlacementError toError(String description) {
        return new OrderPlacementError(description);
    }
}
