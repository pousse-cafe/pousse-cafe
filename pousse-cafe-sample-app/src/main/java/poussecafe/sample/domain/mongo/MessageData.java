package poussecafe.sample.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.sample.domain.ContentType;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.MessageKey;
import poussecafe.storable.BaseProperty;
import poussecafe.storable.Property;

public class MessageData implements Message.Data {

    @Override
    public Property<MessageKey> key() {
        return new BaseProperty<MessageKey>(MessageKey.class) {
            @Override
            protected MessageKey getValue() {
                return key;
            }

            @Override
            protected void setValue(MessageKey value) {
                key = value;
            }

        };
    }

    @Id
    private MessageKey key;

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
