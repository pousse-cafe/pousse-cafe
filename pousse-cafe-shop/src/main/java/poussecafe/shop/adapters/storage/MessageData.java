package poussecafe.shop.adapters.storage;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.shop.domain.ContentType;
import poussecafe.shop.domain.CustomerKey;
import poussecafe.shop.domain.Message;
import poussecafe.shop.domain.MessageKey;

@SuppressWarnings("serial")
public class MessageData implements Message.Attributes, Serializable {

    @Override
    public Attribute<MessageKey> key() {
        return AttributeBuilder.stringKey(MessageKey.class)
                .get(() -> key)
                .set(value -> key = value)
                .build();
    }

    private String key;

    @Override
    public Attribute<CustomerKey> customerKey() {
        return AttributeBuilder.stringKey(CustomerKey.class)
                .get(() -> customerId)
                .set(value -> customerId = value)
                .build();
    }

    private String customerId;

    @Override
    public Attribute<ContentType> contentType() {
        return AttributeBuilder.simple(ContentType.class)
                .get(() -> contentType)
                .set(value -> contentType = value)
                .build();
    }

    private ContentType contentType;
}
