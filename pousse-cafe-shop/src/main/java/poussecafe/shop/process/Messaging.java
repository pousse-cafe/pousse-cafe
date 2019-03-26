package poussecafe.shop.process;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.DomainEvent;
import poussecafe.process.DomainProcess;
import poussecafe.shop.domain.ContentChooser;
import poussecafe.shop.domain.CustomerKey;
import poussecafe.shop.domain.Message;
import poussecafe.shop.domain.MessageFactory;
import poussecafe.shop.domain.MessageRepository;
import poussecafe.shop.domain.OrderCreated;
import poussecafe.shop.domain.OrderReadyForShipping;
import poussecafe.shop.domain.OrderRejected;
import poussecafe.shop.domain.OrderSettled;

public class Messaging extends DomainProcess {

    private MessageFactory factory;

    private MessageRepository repository;

    private ContentChooser contentChooser;

    @MessageListener
    public void createMessage(OrderRejected event) {
        logger.info("Creating message for rejected order");
        createMessageWithContent(event.description().value().customerKey, event);
    }

    protected void createMessageWithContent(CustomerKey customerKey,
            DomainEvent event) {
        Message message = factory.buildMessage(customerKey);
        message.setContentType(contentChooser.chooseContent(event));
        runInTransaction(Message.class, () -> repository.add(message));
    }

    @MessageListener
    public void createMessage(OrderCreated event) {
        logger.info("Creating message for created order");
        createMessageWithContent(event.orderKey().value().getCustomerKey(), event);
    }

    @MessageListener
    public void createMessage(OrderSettled event) {
        logger.info("Creating message for settled order");
        createMessageWithContent(event.orderKey().value().getCustomerKey(), event);
    }

    @MessageListener
    public void createMessage(OrderReadyForShipping event) {
        logger.info("Creating message for order ready-for-shipping");
        createMessageWithContent(event.orderKey().value().getCustomerKey(), event);
    }
}
