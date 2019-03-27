package poussecafe.shop.command;

import poussecafe.attribute.Attribute;
import poussecafe.runtime.Command;
import poussecafe.shop.domain.OrderKey;

public interface ShipOrder extends Command {

    Attribute<OrderKey> orderKey();
}
