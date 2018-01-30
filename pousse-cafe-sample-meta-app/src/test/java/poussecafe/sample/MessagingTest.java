package poussecafe.sample;

import java.util.List;
import org.junit.Test;
import poussecafe.sample.domain.ContentChooser;
import poussecafe.sample.domain.ContentType;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.InMemoryMessageDataAccess;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.MessageData;
import poussecafe.sample.domain.MessageFactory;
import poussecafe.sample.domain.MessageRepository;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderRejected;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.workflow.Messaging;
import poussecafe.storable.StorableDefinition;
import poussecafe.storable.StorableImplementation;
import poussecafe.test.MetaApplicationTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MessagingTest extends MetaApplicationTest {

    private CustomerKey customerKey;

    private ProductKey productKey;

    private OrderDescription orderDescription;

    @Override
    protected void registerComponents() {
        context().environment().defineStorable(new StorableDefinition.Builder()
                .withStorableClass(Message.class)
                .withFactoryClass(MessageFactory.class)
                .withRepositoryClass(MessageRepository.class)
                .build());
        context().environment().implementStorable(new StorableImplementation.Builder()
                .withStorableClass(Message.class)
                .withDataAccessFactory(InMemoryMessageDataAccess::new)
                .withDataFactory(MessageData::new)
                .withStorage(context().getInMemoryStorage())
                .build());

        context().environment().defineProcess(Messaging.class);
        context().environment().defineService(ContentChooser.class);
    }

    @Test
    public void rejectedOrderTriggersNotification() {
        givenOrder();
        whenOrderRejected();
        thenMessageCreatedWithContent(ContentType.ORDER_REJECTED);
    }

    private void givenOrder() {
        customerKey = new CustomerKey("customer-id");
        productKey = new ProductKey("product-id");
        orderDescription = new OrderDescription();
        orderDescription.customerKey = customerKey;
        orderDescription.reference = "ref";
        orderDescription.units = 1;
    }

    private void whenOrderRejected() {
        addDomainEvent(new OrderRejected(productKey, orderDescription));
    }

    private void thenMessageCreatedWithContent(ContentType contentType) {
        MessageRepository repository = (MessageRepository) context().getStorableServices(Message.class).getRepository();
        List<Message> messages = repository.findByCustomer(customerKey);
        assertThat(messages.size(), is(1));
        assertThat(messages.get(0).getContentType(), is(contentType));
    }
}
