package poussecafe.sample.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.property.Property;
import poussecafe.sample.domain.ContentType;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.MessageKey;

public class MessageData implements Message.Data {

    @Override
    public Property<MessageKey> key() {
        return new Property<MessageKey>() {
            @Override
            public MessageKey get() {
                return new MessageKey(key);
            }

            @Override
            public void set(MessageKey value) {
                key = value.getValue();
            }
        };
    }

    @Id
    private String key;

    @Override
    public void setCustomerKey(CustomerKey customerKey) {
        this.customerKey = customerKey;
    }

    private CustomerKey customerKey;

    @Override
    public CustomerKey getCustomerKey() {
        return customerKey;
    }

    @Override
    public void setContentType(ContentType type) {
        contentType = type;
    }

    private ContentType contentType;

    @Override
    public ContentType getContentType() {
        return contentType;
    }

}
