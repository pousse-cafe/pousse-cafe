package poussecafe.shop.command;

import poussecafe.attribute.Attribute;
import poussecafe.runtime.Command;
import poussecafe.shop.domain.ProductKey;

public interface AddUnits extends Command {

    Attribute<ProductKey> productKey();

    Attribute<Integer> units();
}
