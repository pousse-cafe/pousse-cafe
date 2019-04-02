package poussecafe.shop.command;

import poussecafe.attribute.Attribute;
import poussecafe.runtime.Command;
import poussecafe.shop.domain.OrderId;

public interface SettleOrder extends Command {

    Attribute<OrderId> orderId();
}
