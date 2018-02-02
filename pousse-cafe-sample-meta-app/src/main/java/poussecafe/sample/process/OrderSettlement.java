package poussecafe.sample.process;

import poussecafe.process.DomainProcess;
import poussecafe.sample.command.SettleOrder;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderRepository;

public class OrderSettlement extends DomainProcess {

    private OrderRepository orderRepository;

    public void settleOrder(SettleOrder command) {
        runInTransaction(Order.class, () -> {
            Order order = orderRepository.get(command.getOrderKey());
            order.settle();
        });
    }

}
