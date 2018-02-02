package poussecafe.sample.process;

import poussecafe.process.DomainProcess;
import poussecafe.sample.command.ShipOrder;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderRepository;

public class OrderShipment extends DomainProcess {

    private OrderRepository orderRepository;

    public void shipOrder(ShipOrder command) {
        runInTransaction(Order.class, () -> {
            Order order = orderRepository.get(command.getOrderKey());
            order.ship();
        });
    }

}
