package workflow;

import domain.MyAggregateKey;

public class CreateAggregate {

    private MyAggregateKey key;

    public CreateAggregate(MyAggregateKey key) {
        setKey(key);
    }

    public MyAggregateKey getKey() {
        return key;
    }

    private void setKey(MyAggregateKey key) {
        this.key = key;
    }

}
