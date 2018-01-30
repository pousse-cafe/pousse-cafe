package poussecafe.sample.domain;

import poussecafe.inmemory.InlineProperty;
import poussecafe.storable.Property;

public class MessageData implements Message.Data {

    @Override
    public Property<String> key() {
        return key;
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
