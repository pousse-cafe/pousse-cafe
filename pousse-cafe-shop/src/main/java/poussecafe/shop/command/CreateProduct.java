package poussecafe.shop.command;

import poussecafe.attribute.Attribute;
import poussecafe.runtime.Command;
import poussecafe.shop.domain.ProductId;

public interface CreateProduct extends Command {

    Attribute<ProductId> productId();
}
