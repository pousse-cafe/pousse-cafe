package poussecafe.mymodule.adapters;

import java.io.Serializable;
import poussecafe.attribute.Attribute;
import poussecafe.attribute.AttributeBuilder;
import poussecafe.mymodule.domain.myaggregate.MyAggregate;
import poussecafe.mymodule.domain.myaggregate.MyAggregateId;

@SuppressWarnings("serial")
public class MyAggregateData implements MyAggregate.Attributes, Serializable {

    /*
     * AttributeBuilder class exposes factory methods for different types of property builders. The property
     * builders allow to directly expose the value or adapt it before.
     */
    @Override
    public Attribute<MyAggregateId> identifier() {
        return AttributeBuilder.stringId(MyAggregateId.class)
                .read(() -> id)
                .write(value -> id = value)
                .build();
    }

    private String id;

    @Override
    public Attribute<Integer> x() {
        return AttributeBuilder.single(Integer.class)
                .read(() -> x)
                .write(value -> x = value)
                .build();
    }

    private int x;
}
