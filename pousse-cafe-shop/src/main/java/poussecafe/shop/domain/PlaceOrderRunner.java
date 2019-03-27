package poussecafe.shop.domain;

import poussecafe.discovery.SingleAggregateMessageListenerRunner;
import poussecafe.shop.command.PlaceOrder;

public class PlaceOrderRunner extends SingleAggregateMessageListenerRunner<PlaceOrder, ProductKey, Product> {

    @Override
    public ProductKey targetAggregateKey(PlaceOrder message) {
        return message.productKey().value();
    }
}
