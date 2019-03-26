package poussecafe.shop.adapters.storage;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.shop.domain.ContentType;
import poussecafe.shop.domain.CustomerKey;
import poussecafe.shop.domain.Message;
import poussecafe.shop.domain.MessageKey;

@SuppressWarnings("serial")
public class MessageData implements Message.Attributes, Serializable {

    @Override
    public Attribute<MessageKey> key() {
        return new Attribute<MessageKey>() {
            @Override
            public MessageKey value() {
                return new MessageKey(key);
            }

            @Override
            public void value(MessageKey value) {
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
