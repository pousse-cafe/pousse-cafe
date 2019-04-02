package poussecafe.shop.domain;

import poussecafe.discovery.SingleAggregateMessageListenerRunner;
import poussecafe.shop.command.ShipOrder;

public class ShipOrderRunner extends SingleAggregateMessageListenerRunner<ShipOrder, OrderId, Order> {

    @Override
    public OrderId targetAggregateId(ShipOrder message) {
        return message.orderId().value();
    }
}
