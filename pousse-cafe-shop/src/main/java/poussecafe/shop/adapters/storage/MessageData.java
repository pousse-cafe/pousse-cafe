package poussecafe.shop.adapters.storage;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.shop.domain.ContentType;
import poussecafe.shop.domain.CustomerId;
import poussecafe.shop.domain.Message;
import poussecafe.shop.domain.MessageId;

@SuppressWarnings("serial")
public class MessageData implements Message.Attributes, Serializable {

    @Override
    public Attribute<MessageId> identifier() {
        return AttributeBuilder.stringId(MessageId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;

    @Override
    public Attribute<CustomerId> customerId() {
        return AttributeBuilder.stringId(CustomerId.class)
                .read(() -> customerId)
                .write(value -> customerId = value)
                .build();
    }

    private String customerId;

    @Override
    public Attribute<ContentType> contentType() {
        return AttributeBuilder.single(ContentType.class)
                .read(() -> contentType)
                .write(value -> contentType = value)
                .build();
    }

    private ContentType contentType;
}
