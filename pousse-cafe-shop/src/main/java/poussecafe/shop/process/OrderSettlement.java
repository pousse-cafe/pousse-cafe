package poussecafe.shop.process;

import poussecafe.process.DomainProcess;
import poussecafe.shop.command.SettleOrder;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderRepository;

public class OrderSettlement extends DomainProcess {

    private OrderRepository orderRepository;

    public void settleOrder(SettleOrder command) {
        runInTransaction(Order.class, () -> {
            Order order = orderRepository.get(command.getOrderKey());
            order.settle();
        });
    }

}
