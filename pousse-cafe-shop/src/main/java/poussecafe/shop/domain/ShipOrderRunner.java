package poussecafe.shop.domain;

import poussecafe.discovery.SingleAggregateMessageListenerRunner;
import poussecafe.shop.command.ShipOrder;

public class ShipOrderRunner extends SingleAggregateMessageListenerRunner<ShipOrder, OrderKey, Order> {

    @Override
    public OrderKey targetAggregateKey(ShipOrder message) {
        return message.orderKey().value();
    }
}
