package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.shop.domain.MessageCreated;
import poussecafe.shop.domain.MessageId;

@MessageImplementation(message = MessageCreated.class)
@SuppressWarnings("serial")
public class SerializableMessageCreated implements Serializable, MessageCreated {

    @Override
    public Attribute<MessageId> messageId() {
        return AttributeBuilder.single(MessageId.class)
                .from(String.class)
                .adapt(MessageId::new)
                .get(() -> messageId)
                .adapt(MessageId::stringValue)
                .set(value -> messageId = value)
                .build();
    }

    private String messageId;
}
