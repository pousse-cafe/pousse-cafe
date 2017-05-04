package workflow;

import domain.MyAggregateKey;
import poussecafe.messaging.Command;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class MyCommand extends Command {

    private MyAggregateKey key;

    private int x;

    public MyCommand(MyAggregateKey key, int x) {
        setKey(key);
        setX(x);
    }

    public MyAggregateKey getKey() {
        return key;
    }

    private void setKey(MyAggregateKey key) {
        checkThat(value(key).notNull().because("Key cannot be null"));
        this.key = key;
    }

    public int getX() {
        return x;
    }

    private void setX(int x) {
        this.x = x;
    }

}
