package poussecafe.sample.workflow;

import poussecafe.consequence.CommandListener;
import poussecafe.consequence.DomainEventListener;
import poussecafe.process.ProcessManagerKey;
import poussecafe.sample.command.CreateOrder;
import poussecafe.sample.command.PlaceOrder;
import poussecafe.sample.command.StartOrderPlacementProcess;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderCreated;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderFactory;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.OrderPlaced;
import poussecafe.sample.domain.OrderRejected;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.domain.ProductRepository;
import poussecafe.service.Workflow;

public class OrderPlacement extends Workflow {

    private ProductRepository productRepository;

    private OrderFactory orderFactory;

    private OrderRepository orderRepository;

    @CommandListener
    public ProcessManagerKey startOrderPlacementProcess(StartOrderPlacementProcess command) {
        ProcessManagerKey processManagerKey = processManagerKey(command);
        OrderPlacementStateMachine stateMachine = new OrderPlacementStateMachine(command.getProductKey(),
                command.getOrderDescription());
        return startProcess(processManagerKey, stateMachine);
    }

    private ProcessManagerKey processManagerKey(StartOrderPlacementProcess command) {
        return processManagerKey(command.getProductKey(), command.getOrderDescription());
    }

    private ProcessManagerKey processManagerKey(ProductKey productKey,
            OrderDescription orderDescription) {
        return new ProcessManagerKey(
                productKey.getValue() + orderDescription.customerKey.getValue() + orderDescription.reference);
    }

    @CommandListener
    public void placeOrder(PlaceOrder command) {
        runInTransaction(Product.Data.class, () -> {
            Product product = productRepository.get(command.getProductKey());
            product.placeOrder(command.getOrderDescription());
            productRepository.update(product);
        });
    }

    @DomainEventListener
    public void updateProcessManager(OrderPlaced event) {
        ProcessManagerKey processManagerKey = processManagerKey(event);
        executeTransition(processManagerKey, current -> {
            WaitOrderPlaced waitOrderPlaced = (WaitOrderPlaced) current;
            return waitOrderPlaced.toWaitOrderCreated();
        });
    }

    private ProcessManagerKey processManagerKey(OrderPlaced event) {
        return processManagerKey(event.getProductKey(), event.getOrderDescription());
    }

    @CommandListener
    public void createOrder(CreateOrder event) {
        OrderDescription description = event.getOrderDescription();
        OrderKey key = new OrderKey(event.getProductKey(), description.customerKey, description.reference);
        Order order = orderFactory.buildPlacedOrder(key, description.units);
        runInTransaction(Order.Data.class, () -> orderRepository.add(order));
    }

    @DomainEventListener
    public void updateProcessManager(OrderRejected event) {
        ProcessManagerKey processManagerKey = processManagerKey(event);
        executeTransition(processManagerKey, current -> {
            WaitOrderPlaced waitOrderPlaced = (WaitOrderPlaced) current;
            return waitOrderPlaced.toError("Order rejected");
        });
    }

    private ProcessManagerKey processManagerKey(OrderRejected event) {
        return processManagerKey(event.getProductKey(), event.getDescription());
    }

    @DomainEventListener
    public void orderCreated(OrderCreated event) {
        ProcessManagerKey processManagerKey = processManagerKey(event);
        executeTransition(processManagerKey, current -> {
            WaitOrderCreated waitOrderCreated = (WaitOrderCreated) current;
            return waitOrderCreated.toFinal();
        });
    }

    private ProcessManagerKey processManagerKey(OrderCreated event) {
        OrderDescription orderDescription = new OrderDescription();
        orderDescription.customerKey = event.getOrderKey().getCustomerKey();
        orderDescription.reference = event.getOrderKey().getReference();
        return processManagerKey(event.getOrderKey().getProductKey(), orderDescription);
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
