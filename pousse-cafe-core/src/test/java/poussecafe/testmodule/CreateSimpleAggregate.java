package poussecafe.testmodule;

import poussecafe.attribute.Attribute;
import poussecafe.runtime.Command;

public interface CreateSimpleAggregate extends Command {

    Attribute<SimpleAggregateId> identifier();

    Attribute<String> data();
}
