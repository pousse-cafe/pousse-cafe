package poussecafe.sample.workflow;

import poussecafe.sample.command.ShipOrder;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderRepository;

public class OrderShipment extends poussecafe.service.Process {

    private OrderRepository orderRepository;

    public void shipOrder(ShipOrder command) {
        runInTransaction(Order.Data.class, () -> {
            Order order = orderRepository.get(command.getOrderKey());
            order.ship();
        });
    }

}
