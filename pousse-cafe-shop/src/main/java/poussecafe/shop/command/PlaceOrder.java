package poussecafe.shop.command;

import poussecafe.attribute.Attribute;
import poussecafe.runtime.Command;
import poussecafe.shop.domain.OrderDescription;
import poussecafe.shop.domain.ProductId;

public interface PlaceOrder extends Command {

    Attribute<ProductId> productId();

    Attribute<OrderDescription> description();
}
