package poussecafe.shop.domain;

import poussecafe.discovery.SingleAggregateMessageListenerRunner;
import poussecafe.shop.command.SettleOrder;

public class SettleRunner extends SingleAggregateMessageListenerRunner<SettleOrder, OrderKey, Order> {

    @Override
    public OrderKey targetAggregateKey(SettleOrder message) {
        return message.orderKey().value();
    }
}
