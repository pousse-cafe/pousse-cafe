package poussecafe.sample.workflow;

import poussecafe.sample.command.SettleOrder;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderRepository;

public class OrderSettlement extends poussecafe.service.Process {

    private OrderRepository orderRepository;

    public void settleOrder(SettleOrder command) {
        runInTransaction(Order.Data.class, () -> {
            Order order = orderRepository.get(command.getOrderKey());
            order.settle();
        });
    }

}
