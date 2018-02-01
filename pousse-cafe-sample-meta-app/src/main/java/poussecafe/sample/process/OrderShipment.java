package poussecafe.sample.process;

import poussecafe.sample.command.ShipOrder;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.service.DomainProcess;

public class OrderShipment extends DomainProcess {

    private OrderRepository orderRepository;

    public void shipOrder(ShipOrder command) {
        runInTransaction(Order.class, () -> {
            Order order = orderRepository.get(command.getOrderKey());
            order.ship();
        });
    }

}
