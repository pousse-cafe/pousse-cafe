package poussecafe.sample.adapters.messaging;

import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;
import poussecafe.sample.domain.MessageCreated;
import poussecafe.sample.domain.MessageKey;

public class JacksonMessageCreated implements MessageCreated {

    @Override
    public Property<MessageKey> messageKey() {
        return PropertyBuilder.simple(MessageKey.class)
                .from(String.class)
                .adapt(MessageKey::new)
                .get(() -> messageId)
                .adapt(MessageKey::getValue)
                .set(value -> messageId = value)
                .build();
    }

    private String messageId;
}
