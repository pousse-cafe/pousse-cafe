package poussecafe.sample;

import java.util.List;
import org.junit.Test;
import poussecafe.sample.configuration.MessageConfiguration;
import poussecafe.sample.domain.ContentChooser;
import poussecafe.sample.domain.ContentType;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.MessageRepository;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderRejected;
import poussecafe.sample.domain.ProductKey;
import poussecafe.sample.workflow.Messaging;
import poussecafe.storage.InMemoryStorage;
import poussecafe.test.MetaApplicationTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MessagingTest extends MetaApplicationTest {

    private CustomerKey customerKey;

    private ProductKey productKey;

    private OrderDescription orderDescription;

    @Override
    protected void registerComponents() {
        configuration.registerAggregate(new MessageConfiguration());
        configuration.registerWorkflow(new Messaging());
        configuration.registerService(new ContentChooser());

        InMemoryStorage storage = (InMemoryStorage) configuration.getDefaultStorage();
        storage.registerDataAccess(Message.Data.class, new InMemoryMessageDataAccess());
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
