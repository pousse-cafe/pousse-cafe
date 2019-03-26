package poussecafe.shop.process;

import poussecafe.discovery.MessageListener;
import poussecafe.process.DomainProcess;
import poussecafe.shop.command.PlaceOrder;
import poussecafe.shop.domain.Order;
import poussecafe.shop.domain.OrderDescription;
import poussecafe.shop.domain.OrderFactory;
import poussecafe.shop.domain.OrderKey;
import poussecafe.shop.domain.OrderPlaced;
import poussecafe.shop.domain.OrderRepository;
import poussecafe.shop.domain.Product;
import poussecafe.shop.domain.ProductRepository;

public class OrderPlacement extends DomainProcess {

    private ProductRepository productRepository;

    private OrderFactory orderFactory;

    private OrderRepository orderRepository;

    public void placeOrder(PlaceOrder command) {
        runInTransaction(Product.class, () -> {
            Product product = productRepository.get(command.getProductKey());
            product.placeOrder(command.getOrderDescription());
            productRepository.update(product);
        });
    }

    @MessageListener
    public void updateProcessManager(OrderPlaced event) {
        OrderDescription description = event.description().value();
        OrderKey key = new OrderKey(event.productKey().value(), description.customerKey, description.reference);
        Order order = orderFactory.buildPlacedOrder(key, description.units);
        runInTransaction(Order.class, () -> orderRepository.add(order));
    }

}
