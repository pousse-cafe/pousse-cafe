package poussecafe.shop.domain;

import java.util.Set;
import poussecafe.discovery.DefaultAggregateMessageListenerRunner;
import poussecafe.shop.command.AddUnits;

import static poussecafe.collection.Collections.asSet;

public class AddUnitsRunner extends DefaultAggregateMessageListenerRunner<AddUnits, ProductKey, Product> {

    @Override
    public Set<ProductKey> targetAggregatesKeys(AddUnits message) {
        return asSet(message.productKey().value());
    }
}
