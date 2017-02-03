package poussecafe.sample.domain;

import poussecafe.domain.AggregateData;
import poussecafe.domain.AggregateRoot;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class Message extends AggregateRoot<MessageKey, Message.Data> {

    void setCustomerKey(CustomerKey customerKey) {
        checkThat(value(customerKey).notNull().because("Customer key cannot be null"));
        getData().setCustomerKey(customerKey);
    }

    public void setContentType(ContentType type) {
        checkThat(value(type).notNull().because("Content type cannot be null"));
        getData().setContentType(type);
    }

    public ContentType getContentType() {
        return getData().getContentType();
    }

    public interface Data extends AggregateData<MessageKey> {

        void setCustomerKey(CustomerKey customerKey);

        CustomerKey getCustomerKey();

        void setContentType(ContentType type);

        ContentType getContentType();
    }

}
