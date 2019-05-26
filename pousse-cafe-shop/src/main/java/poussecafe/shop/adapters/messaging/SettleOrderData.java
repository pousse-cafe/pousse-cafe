package poussecafe.shop.adapters.messaging;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.discovery.MessageImplementation;
import poussecafe.shop.adapters.storage.OrderIdData;
import poussecafe.shop.command.SettleOrder;
import poussecafe.shop.domain.OrderId;

@MessageImplementation(message = SettleOrder.class)
@SuppressWarnings("serial")
public class SettleOrderData implements Serializable, SettleOrder {

    @Override
    public Attribute<OrderId> orderId() {
        return AttributeBuilder.single(OrderId.class)
                .usingAutoAdapter(OrderIdData.class)
                .read(() -> orderId)
                .write(value -> orderId = value)
                .build();
    }

    private OrderIdData orderId;
}
