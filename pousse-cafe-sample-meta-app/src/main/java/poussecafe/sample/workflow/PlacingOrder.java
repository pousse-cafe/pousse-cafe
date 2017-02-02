package poussecafe.sample.workflow;

import poussecafe.consequence.CommandListener;
import poussecafe.consequence.DomainEventListener;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderFactory;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.OrderPlaced;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductFactory;
import poussecafe.sample.domain.ProductRepository;
import poussecafe.service.Workflow;

public class PlacingOrder extends Workflow {

    private ProductFactory productFactory;

    private ProductRepository productRepository;

    private OrderFactory orderFactory;

    private OrderRepository orderRepository;

    @CommandListener
    public void createProduct(CreateProduct command) {
        Product product = productFactory.buildProductWithNoStock(command.getProductKey());
        runInTransaction(() -> productRepository.add(product));
    }

    @CommandListener
    public void addUnits(AddUnits command) {
        runInTransaction(() -> {
            Product product = productRepository.get(command.getProductKey());
            product.addUnits(command.getUnits());
            productRepository.update(product);
        });
    }

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
        OrderKey key = new OrderKey(event.getProductKey(), description.reference);
        Order order = orderFactory.buildPlacedOrder(key, event.getProductKey(), description.units);
        runInTransaction(() -> orderRepository.add(order));
    }

    public void setProductFactory(ProductFactory productFactory) {
        this.productFactory = productFactory;
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
