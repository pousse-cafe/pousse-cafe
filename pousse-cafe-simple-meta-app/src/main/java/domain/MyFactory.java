package domain;

import poussecafe.domain.Factory;

/*
 * The Factory is responsible for providing a shallow (i.e. with no data nor key defined) instance of Aggregate.
 * It also exposes any means of building an aggregate in a consistent initial state.
 */
public class MyFactory extends Factory<MyAggregateKey, MyAggregate, MyAggregate.Data> {

    public MyAggregate buildAggregate(MyAggregateKey key) {
        return newAggregateWithKey(key);
    }

}
