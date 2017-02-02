package poussecafe.sample.workflow;

import poussecafe.consequence.CommandListener;
import poussecafe.consequence.DomainEventListener;
import poussecafe.sample.command.PlaceOrder;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderFactory;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.OrderPlaced;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.sample.domain.OrderSettled;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductRepository;
import poussecafe.service.Workflow;

public class OrderManagement extends Workflow {

    private ProductRepository productRepository;

    private OrderFactory orderFactory;

    private OrderRepository orderRepository;

    @CommandListener
    public void placeOrder(PlaceOrder command) {
        runInTransaction(() -> {
            Product product = productRepository.get(command.getProductKey());
            product.placeOrder(command.getOrderDescription());
            productRepository.update(product);
        });
    }

    @DomainEventListener
    public void createOrder(OrderPlaced event) {
        OrderDescription description = event.getOrderDescription();
        OrderKey key = new OrderKey(event.getProductKey(), description.customerKey, description.reference);
        Order order = orderFactory.buildPlacedOrder(key, description.units);
        runInTransaction(() -> orderRepository.add(order));
    }

    @DomainEventListener
    public void sendOrder(OrderSettled event) {
        runInTransaction(() -> {
            Order order = orderRepository.get(event.getOrderKey());
            order.send();
            orderRepository.update(order);
        });
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void setOrderFactory(OrderFactory orderFactory) {
        this.orderFactory = orderFactory;
    }

    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

}
