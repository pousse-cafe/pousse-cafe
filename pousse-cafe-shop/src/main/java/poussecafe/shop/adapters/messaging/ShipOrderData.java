package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.shop.adapters.storage.OrderKeyData;
import poussecafe.shop.command.ShipOrder;
import poussecafe.shop.domain.OrderKey;

@MessageImplementation(message = ShipOrder.class)
@SuppressWarnings("serial")
public class ShipOrderData implements Serializable, ShipOrder {

    @Override
    public Attribute<OrderKey> orderKey() {
        return AttributeBuilder.simple(OrderKey.class)
                .fromAutoAdapting(OrderKeyData.class)
                .get(() -> orderKey)
                .set(value -> orderKey = value)
                .build();
    }

    private OrderKeyData orderKey;
}
