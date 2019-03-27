package poussecafe.shop.command;

import poussecafe.attribute.Attribute;
import poussecafe.runtime.Command;
import poussecafe.shop.domain.OrderDescription;
import poussecafe.shop.domain.ProductKey;

public interface PlaceOrder extends Command {

    Attribute<ProductKey> productKey();

    Attribute<OrderDescription> description();
}
