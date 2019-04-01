package poussecafe.myboundedcontext.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregate;
import poussecafe.myboundedcontext.domain.myaggregate.MyAggregateKey;

@SuppressWarnings("serial")
public class MyAggregateData implements MyAggregate.Attributes, Serializable {

    /*
     * AttributeBuilder class exposes factory methods for different types of property builders. The property
     * builders allow to directly expose the value or adapt it before.
     */
    @Override
    public Attribute<MyAggregateKey> key() {
        return AttributeBuilder.stringKey(MyAggregateKey.class)
                .get(() -> key)
                .set(value -> key = value)
                .build();
    }

    private String key;

    @Override
    public Attribute<Integer> x() {
        return AttributeBuilder.single(Integer.class)
                .get(() -> x)
                .set(value -> x = value)
                .build();
    }

    private int x;
}
