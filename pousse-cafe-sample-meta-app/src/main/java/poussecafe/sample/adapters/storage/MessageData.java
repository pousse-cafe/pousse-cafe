package poussecafe.sample.adapters.storage;

import java.io.Serializable;
import poussecafe.property.Property;
import poussecafe.sample.domain.ContentType;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.MessageKey;

@SuppressWarnings("serial")
public class MessageData implements Message.Data, Serializable {

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

    private String key;

    @Override
    public void setCustomerKey(CustomerKey customerKey) {
        this.customerKey = customerKey.getValue();
    }

    private String customerKey;

    @Override
    public CustomerKey getCustomerKey() {
        return new CustomerKey(customerKey);
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
