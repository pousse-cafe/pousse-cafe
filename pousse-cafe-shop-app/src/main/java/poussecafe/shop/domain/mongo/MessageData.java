package poussecafe.shop.domain.mongo;

import org.springframework.data.annotation.Id;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.shop.domain.ContentType;
import poussecafe.shop.domain.CustomerId;
import poussecafe.shop.domain.Message;
import poussecafe.shop.domain.MessageId;

public class MessageData implements Message.Attributes {

    @Override
    public Attribute<MessageId> identifier() {
        return AttributeBuilder.stringId(MessageId.class)
                .get(() -> id)
                .set(value -> id = value)
                .build();
    }

    @Id
    private String id;

    @Override
    public Attribute<CustomerId> customerId() {
        return AttributeBuilder.stringId(CustomerId.class)
                .get(() -> customerId)
                .set(value -> customerId = value)
                .build();
    }

    private String customerId;

    @Override
    public Attribute<ContentType> contentType() {
        return AttributeBuilder.single(ContentType.class)
                .get(() -> contentType)
                .set(value -> contentType = value)
                .build();
    }

    private ContentType contentType;
}
