package poussecafe.testmodule;

import poussecafe.attribute.Attribute;
import poussecafe.runtime.Command;

public interface UpdateSimpleAggregate extends Command {

    Attribute<SimpleAggregateId> identifier();
}
