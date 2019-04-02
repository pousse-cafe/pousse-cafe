package poussecafe.shop.domain;

import poussecafe.discovery.SingleAggregateMessageListenerRunner;
import poussecafe.shop.command.SettleOrder;

public class SettleRunner extends SingleAggregateMessageListenerRunner<SettleOrder, OrderId, Order> {

    @Override
    public OrderId targetAggregateId(SettleOrder message) {
        return message.orderId().value();
    }
}
