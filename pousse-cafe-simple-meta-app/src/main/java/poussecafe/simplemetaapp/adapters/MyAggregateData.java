package poussecafe.simplemetaapp.adapters;

import java.io.Serializable;
import poussecafe.property.Property;
import poussecafe.property.PropertyBuilder;
import poussecafe.simplemetaapp.domain.MyAggregate;
import poussecafe.simplemetaapp.domain.MyAggregateKey;

@SuppressWarnings("serial")
public class MyAggregateData implements MyAggregate.Data, Serializable {

    /*
     * PropertyBuilder class exposes factory methods for different types of property builders. The property
     * builders allow to directly expose the value or adapt it before.
     */
    @Override
    public Property<MyAggregateKey> key() {
        return PropertyBuilder.simple(MyAggregateKey.class)
                .from(String.class)
                .adapt(MyAggregateKey::new)
                .get(() -> key)
                .adapt(MyAggregateKey::getValue)
                .set(value -> key = value)
                .build();
    }

    private String key;

    @Override
    public Property<Integer> x() {
        return PropertyBuilder.simple(Integer.class)
                .get(() -> x)
                .set(value -> x = value)
                .build();
    }

    private int x;
}
