package poussecafe.domain;

import poussecafe.attribute.Attribute;

public class SimpleAggregateData implements SimpleAggregate.Attributes {

    @Override
    public Attribute<SimpleAggregateKey> key() {
        return new Attribute<SimpleAggregateKey>() {
            @Override
            public SimpleAggregateKey value() {
                return new SimpleAggregateKey(key);
            }

            @Override
            public void value(SimpleAggregateKey value) {
                key = value.getId();
            }
        };
    }

    private String key;
}
