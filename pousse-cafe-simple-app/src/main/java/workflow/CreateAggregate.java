package workflow;

import domain.MyAggregateKey;
import poussecafe.consequence.Command;

public class CreateAggregate extends Command {

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
