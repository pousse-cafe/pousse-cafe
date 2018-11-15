package poussecafe.sample.adapters.messaging;

import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;
import poussecafe.sample.domain.OrderKey;
import poussecafe.sample.domain.OrderSettled;

public class JacksonOrderSettled implements OrderSettled {

    @Override
    public Property<OrderKey> orderKey() {
        return PropertyBuilder.simple(OrderKey.class)
                .from(JacksonOrderKey.class)
                .adapt(JacksonOrderKey::adapt)
                .get(() -> orderKey)
                .adapt(JacksonOrderKey::adapt)
                .set(value -> orderKey = value)
                .build();
    }

    private JacksonOrderKey orderKey;
}
