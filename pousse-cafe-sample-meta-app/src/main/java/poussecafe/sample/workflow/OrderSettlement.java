package poussecafe.sample.workflow;

import poussecafe.consequence.CommandListener;
import poussecafe.sample.command.SettleOrder;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.service.Workflow;

public class OrderSettlement extends Workflow {

    private OrderRepository orderRepository;

    @CommandListener
    public void settleOrder(SettleOrder command) {
        runInTransaction(() -> {
            Order order = orderRepository.get(command.getOrderKey());
            order.settle();
        });
    }

    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

}
