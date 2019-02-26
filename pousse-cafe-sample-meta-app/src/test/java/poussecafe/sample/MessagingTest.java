package poussecafe.sample;

import java.util.List;
import org.junit.Test;
import poussecafe.environment.BoundedContext;
import poussecafe.sample.adapters.messaging.SerializableOrderRejected;
import poussecafe.sample.domain.ContentType;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.MessageRepository;
import poussecafe.sample.domain.OrderDescription;
import poussecafe.sample.domain.OrderRejected;
import poussecafe.sample.domain.ProductKey;
import poussecafe.test.PousseCafeTest;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MessagingTest extends PousseCafeTest {

    private CustomerKey customerKey;

    private ProductKey productKey;

    private OrderDescription orderDescription;

    @Override
    protected List<BoundedContext> boundedContexts() {
        return asList(SampleBoundedContextDefinition.configure().defineAndImplementDefault().build());
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
        OrderRejected event = new SerializableOrderRejected();
        event.productKey().value(productKey);
        event.description().value(orderDescription);
        addDomainEvent(event);
    }

    private void thenMessageCreatedWithContent(ContentType contentType) {
        waitUntilAllMessageQueuesEmpty();
        List<Message> messages = messageRepository.findByCustomer(customerKey);
        assertThat(messages.size(), is(1));
        assertThat(messages.get(0).getContentType(), is(contentType));
    }

    private MessageRepository messageRepository;
}
