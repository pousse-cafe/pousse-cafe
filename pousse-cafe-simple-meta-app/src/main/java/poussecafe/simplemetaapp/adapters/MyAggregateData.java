package poussecafe.simplemetaapp.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.simplemetaapp.domain.MyAggregate;
import poussecafe.simplemetaapp.domain.MyAggregateKey;

@SuppressWarnings("serial")
public class MyAggregateData implements MyAggregate.Attributes, Serializable {

    /*
     * AttributeBuilder class exposes factory methods for different types of property builders. The property
     * builders allow to directly expose the value or adapt it before.
     */
    @Override
    public Attribute<MyAggregateKey> key() {
        return AttributeBuilder.simple(MyAggregateKey.class)
                .from(String.class)
                .adapt(MyAggregateKey::new)
                .get(() -> key)
                .adapt(MyAggregateKey::getValue)
                .set(value -> key = value)
                .build();
    }

    private String key;

    @Override
    public Attribute<Integer> x() {
        return AttributeBuilder.simple(Integer.class)
                .get(() -> x)
                .set(value -> x = value)
                .build();
    }

    private int x;
}
