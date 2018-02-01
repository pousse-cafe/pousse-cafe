package poussecafe.sample.domain.data;

import java.io.Serializable;
import poussecafe.inmemory.InlineProperty;
import poussecafe.sample.domain.ContentType;
import poussecafe.sample.domain.CustomerKey;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.MessageKey;
import poussecafe.storable.ConvertingProperty;
import poussecafe.storable.Property;

public class MessageData implements Message.Data, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public Property<MessageKey> key() {
        return new ConvertingProperty<String, MessageKey>(key, MessageKey.class) {
            @Override
            protected MessageKey convertFrom(String from) {
                return new MessageKey(from);
            }

            @Override
            protected String convertTo(MessageKey to) {
                return to.getValue();
            }
        };
    }

    private InlineProperty<String> key = new InlineProperty<>(String.class);

    @Override
    public void setCustomerKey(CustomerKey customerKey) {
        this.customerKey.set(customerKey.getValue());
    }

    private InlineProperty<String> customerKey = new InlineProperty<>(String.class);

    @Override
    public CustomerKey getCustomerKey() {
        return new CustomerKey(customerKey.get());
    }

    @Override
    public void setContentType(ContentType type) {
        contentType.set(type.name());
    }

    private InlineProperty<String> contentType = new InlineProperty<>(String.class);

    @Override
    public ContentType getContentType() {
        return ContentType.valueOf(contentType.get());
    }

}
