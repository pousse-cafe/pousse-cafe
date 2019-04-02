package poussecafe.shop.command;

import poussecafe.attribute.Attribute;
import poussecafe.runtime.Command;
import poussecafe.shop.domain.OrderId;

public interface ShipOrder extends Command {

    Attribute<OrderId> orderId();
}
