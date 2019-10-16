package poussecafe.mymodule;

import poussecafe.attribute.Attribute;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;
import poussecafe.runtime.Command;

public interface ACommand extends Command {

    Attribute<MyAggregateId> id();

    Attribute<Integer> x();
}
