package poussecafe.shop.domain;

import poussecafe.discovery.SingleAggregateMessageListenerRunner;
import poussecafe.shop.command.PlaceOrder;

public class PlaceOrderRunner extends SingleAggregateMessageListenerRunner<PlaceOrder, ProductId, Product> {

    @Override
    public ProductId targetAggregateId(PlaceOrder message) {
        return message.productId().value();
    }
}
