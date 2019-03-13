package poussecafe.sample.process;

import poussecafe.discovery.MessageListener;
import poussecafe.domain.DomainEvent;
import poussecafe.process.DomainProcess;
import poussecafe.sample.domain.ContentChooser;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.MessageFactory;
import poussecafe.sample.domain.MessageRepository;
import poussecafe.sample.domain.OrderCreated;
import poussecafe.sample.domain.OrderReadyForShipping;
import poussecafe.sample.domain.OrderRejected;
import poussecafe.sample.domain.OrderSettled;

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
