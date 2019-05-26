package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.shop.domain.OrderCreated;
import poussecafe.shop.domain.OrderId;

@MessageImplementation(message = OrderCreated.class)
@SuppressWarnings("serial")
public class SerializableOrderCreated implements Serializable, OrderCreated {

    @Override
    public Attribute<OrderId> orderId() {
        return AttributeBuilder.single(OrderId.class)
                .storedAs(SerializableOrderId.class)
                .adaptOnRead(SerializableOrderId::adapt)
                .read(() -> orderId)
                .adaptOnWrite(SerializableOrderId::adapt)
                .write(value -> orderId = value)
                .build();
    }

    private SerializableOrderId orderId;
}
