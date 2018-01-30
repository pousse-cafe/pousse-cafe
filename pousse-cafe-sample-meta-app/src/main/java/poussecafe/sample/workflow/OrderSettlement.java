package poussecafe.sample.workflow;

import poussecafe.sample.command.SettleOrder;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.service.Workflow;

public class OrderSettlement extends Workflow {

    private OrderRepository orderRepository;

    public void settleOrder(SettleOrder command) {
        runInTransaction(Order.Data.class, () -> {
            Order order = orderRepository.get(command.getOrderKey());
            order.settle();
        });
    }

}
