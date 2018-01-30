package poussecafe.sample.domain;

import poussecafe.domain.AggregateRoot;
import poussecafe.storable.Property;
import poussecafe.storable.StorableData;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class Message extends AggregateRoot<MessageKey, Message.Data> {

    @Override
    public MessageKey getKey() {
        return new MessageKey(getData().key().get());
    }

    @Override
    public void setKey(MessageKey key) {
        getData().key().set(key.getValue());
    }

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

    public static interface Data extends StorableData {

        Property<String> key();

        void setCustomerKey(CustomerKey customerKey);

        CustomerKey getCustomerKey();

        void setContentType(ContentType type);

        ContentType getContentType();
    }

}
