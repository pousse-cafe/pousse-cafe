package poussecafe.sample.workflow;

import poussecafe.consequence.CommandListener;
import poussecafe.sample.command.ShipOrder;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.service.Workflow;

public class OrderShipment extends Workflow {

    private OrderRepository orderRepository;

    @CommandListener
    public void shipOrder(ShipOrder command) {
        runInTransaction(() -> {
            Order order = orderRepository.get(command.getOrderKey());
            order.ship();
        });
    }

    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

}
