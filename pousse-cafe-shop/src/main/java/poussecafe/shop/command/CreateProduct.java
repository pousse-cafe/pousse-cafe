package poussecafe.shop.command;

import poussecafe.attribute.Attribute;
import poussecafe.runtime.Command;
import poussecafe.shop.domain.ProductKey;

public interface CreateProduct extends Command {

    Attribute<ProductKey> productKey();
}
