package poussecafe.shop.command;

import poussecafe.attribute.Attribute;
import poussecafe.runtime.Command;
import poussecafe.shop.domain.ProductId;

public interface AddUnits extends Command {

    Attribute<ProductId> productId();

    Attribute<Integer> units();
}
