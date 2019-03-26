package poussecafe.shop.process;

import poussecafe.process.DomainProcess;
import poussecafe.shop.command.ShipOrder;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderRepository;

public class OrderShipment extends DomainProcess {

    private OrderRepository orderRepository;

    public void shipOrder(ShipOrder command) {
        runInTransaction(Order.class, () -> {
            Order order = orderRepository.get(command.getOrderKey());
            order.ship();
        });
    }

}
