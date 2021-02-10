package poussecafe.source.validation.types.adapters;

import poussecafe.attribute.Attribute;
import poussecafe.source.validation.types.MyAggregate;

public class MyAggregateAttributes implements MyAggregate.Root.Attributes {

    @Override
    public Attribute<String> identifier() {
        return null;
    }
}
