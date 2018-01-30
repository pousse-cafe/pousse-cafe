package poussecafe.sample.workflow;

import poussecafe.sample.command.ShipOrder;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.service.Workflow;

public class OrderShipment extends Workflow {

    private OrderRepository orderRepository;

    public void shipOrder(ShipOrder command) {
        runInTransaction(Order.Data.class, () -> {
            Order order = orderRepository.get(command.getOrderKey());
            order.ship();
        });
    }

}
