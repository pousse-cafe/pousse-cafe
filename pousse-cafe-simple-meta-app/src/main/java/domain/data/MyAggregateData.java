package domain.data;

import domain.MyAggregate;
import domain.MyAggregateKey;
import java.io.Serializable;
import poussecafe.storable.ConvertingProperty;
import poussecafe.storable.Property;
import poussecafe.storage.memory.InlineProperty;

public class MyAggregateData implements MyAggregate.Data, Serializable {

    private static final long serialVersionUID = 6899835081195830873L;

    @Override
    public Property<MyAggregateKey> key() {
        return new ConvertingProperty<String, MyAggregateKey>(key) {
            @Override
            protected MyAggregateKey convertFrom(String from) {
                return new MyAggregateKey(from);
            }

            @Override
            protected String convertTo(MyAggregateKey to) {
                return to.getValue();
            }
        };
    }

    private InlineProperty<String> key = new InlineProperty<>(String.class);

    @Override
    public void setX(int x) {
        this.x.set(x);
    }

    private InlineProperty<Integer> x = new InlineProperty<>(Integer.class);

    @Override
    public int getX() {
        return x.get();
    }

}
