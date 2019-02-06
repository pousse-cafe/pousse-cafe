package poussecafe.sample.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.contextconfigurer.MessageImplementation;
import poussecafe.sample.domain.MessageCreated;
import poussecafe.sample.domain.MessageKey;

@MessageImplementation(message = MessageCreated.class)
@SuppressWarnings("serial")
public class SerializableMessageCreated implements Serializable, MessageCreated {

    @Override
    public Attribute<MessageKey> messageKey() {
        return AttributeBuilder.simple(MessageKey.class)
                .from(String.class)
                .adapt(MessageKey::new)
                .get(() -> messageId)
                .adapt(MessageKey::getValue)
                .set(value -> messageId = value)
                .build();
    }

    private String messageId;
}
