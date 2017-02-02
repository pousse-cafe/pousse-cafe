package poussecafe.sample.domain;

import poussecafe.domain.Repository;
import poussecafe.sample.domain.Order.OrderData;

public class OrderRepository extends Repository<Order, OrderKey, OrderData> {

    @Override
    protected Order newAggregate() {
        return new Order();
    }

}
