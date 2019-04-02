package poussecafe.shop.domain;

import java.util.Set;
import poussecafe.discovery.DefaultAggregateMessageListenerRunner;
import poussecafe.shop.command.AddUnits;

import static poussecafe.collection.Collections.asSet;

public class AddUnitsRunner extends DefaultAggregateMessageListenerRunner<AddUnits, ProductId, Product> {

    @Override
    public Set<ProductId> targetAggregatesIds(AddUnits message) {
        return asSet(message.productId().value());
    }
}
