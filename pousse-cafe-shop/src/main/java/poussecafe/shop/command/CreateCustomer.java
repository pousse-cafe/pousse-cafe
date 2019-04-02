package poussecafe.shop.command;

import poussecafe.attribute.Attribute;
import poussecafe.runtime.Command;
import poussecafe.shop.domain.CustomerId;

public interface CreateCustomer extends Command {

    Attribute<CustomerId> customerId();
}
