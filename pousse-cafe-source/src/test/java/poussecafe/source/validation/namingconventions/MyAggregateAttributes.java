package poussecafe.source.validation.namingconventions;

import poussecafe.attribute.Attribute;

public class MyAggregateAttributes implements MyAggregate.Root.Attributes {

    @Override
    public Attribute<String> identifier() {
        return null;
    }
}
