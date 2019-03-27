package poussecafe.shop.command;

import poussecafe.attribute.Attribute;
import poussecafe.runtime.Command;
import poussecafe.shop.domain.CustomerKey;

public interface CreateCustomer extends Command {

    Attribute<CustomerKey> customerKey();
}
