package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.shop.domain.OrderId;
import poussecafe.shop.domain.OrderSettled;

@MessageImplementation(message = OrderSettled.class)
@SuppressWarnings("serial")
public class SerializableOrderSettled implements Serializable, OrderSettled {

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
