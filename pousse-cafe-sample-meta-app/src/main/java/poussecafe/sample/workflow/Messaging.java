package poussecafe.sample.workflow;

import poussecafe.consequence.DomainEventListener;
import poussecafe.domain.DomainEvent;
import poussecafe.sample.domain.ContentChooser;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.MessageFactory;
import poussecafe.sample.domain.MessageRepository;
import poussecafe.sample.domain.OrderCreated;
import poussecafe.sample.domain.OrderReadyForShipping;
import poussecafe.sample.domain.OrderRejected;
import poussecafe.sample.domain.OrderSettled;
import poussecafe.service.Workflow;

public class Messaging extends Workflow {

    private MessageFactory factory;

    private MessageRepository repository;

    private ContentChooser contentChooser;

    @DomainEventListener
    public void createMessage(OrderRejected event) {
        createMessageWithContent(event.getDescription().customerKey, event);
    }

    protected void createMessageWithContent(CustomerKey customerKey,
            DomainEvent event) {
        Message message = factory.buildMessage(customerKey);
        message.setContentType(contentChooser.chooseContent(event));
        runInTransaction(Message.Data.class, () -> repository.add(message));
    }

    @DomainEventListener
    public void createMessage(OrderCreated event) {
        createMessageWithContent(event.getOrderKey().getCustomerKey(), event);
    }

    @DomainEventListener
    public void createMessage(OrderSettled event) {
        createMessageWithContent(event.getOrderKey().getCustomerKey(), event);
    }

    @DomainEventListener
    public void createMessage(OrderReadyForShipping event) {
        createMessageWithContent(event.getOrderKey().getCustomerKey(), event);
    }

    public void setFactory(MessageFactory factory) {
        this.factory = factory;
    }

    public void setRepository(MessageRepository repository) {
        this.repository = repository;
    }

    public void setContentChooser(ContentChooser contentChooser) {
        this.contentChooser = contentChooser;
    }
}
