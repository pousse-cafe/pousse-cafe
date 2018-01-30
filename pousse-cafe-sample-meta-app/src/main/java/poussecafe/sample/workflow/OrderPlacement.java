package poussecafe.sample.workflow;

import poussecafe.messaging.DomainEventListener;
import poussecafe.sample.command.PlaceOrder;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderFactory;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.OrderPlaced;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductRepository;
import poussecafe.service.Process;

public class OrderPlacement extends Process {

    private ProductRepository productRepository;

    private OrderFactory orderFactory;

    private OrderRepository orderRepository;

    public void placeOrder(PlaceOrder command) {
        runInTransaction(Product.Data.class, () -> {
            Product product = productRepository.get(command.getProductKey());
            product.placeOrder(command.getOrderDescription());
            productRepository.update(product);
        });
    }

    @DomainEventListener
    public void updateProcessManager(OrderPlaced event) {
        OrderDescription description = event.getOrderDescription();
        OrderKey key = new OrderKey(event.getProductKey(), description.customerKey, description.reference);
        Order order = orderFactory.buildPlacedOrder(key, description.units);
        runInTransaction(Order.class, () -> orderRepository.add(order));
    }

}
