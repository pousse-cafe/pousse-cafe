package domain.data;

import domain.MyAggregate;
import domain.MyAggregateKey;
import poussecafe.storable.Property;
import poussecafe.storage.memory.InMemoryActiveData;

@SuppressWarnings("serial")
public class MyAggregateData extends InMemoryActiveData<MyAggregateKey> implements MyAggregate.Data {

    @Override
    public Property<MyAggregateKey> key() {
        return new Property<MyAggregateKey>() {
            @Override
            public MyAggregateKey get() {
                return new MyAggregateKey(key);
            }

            @Override
            public void set(MyAggregateKey value) {
                key = value.getValue();
            }
        };
    }

    private String key;

    @Override
    public void setX(int x) {
        this.x = x;
    }

    private int x;

    @Override
    public int getX() {
        return x;
    }

}
