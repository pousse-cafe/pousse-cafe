package poussecafe.sample.domain;

import poussecafe.domain.Repository;
import poussecafe.sample.domain.Order.Data;
import poussecafe.storable.UnitOfConsequence;

public class OrderRepository extends Repository<Order, OrderKey, Data> {

    @Override
    protected Order newAggregate() {
        return new Order();
    }

    @Override
    protected void considerUnitEmissionAfterAdd(Order order,
            UnitOfConsequence unitOfConsequence) {
        unitOfConsequence.addConsequence(new OrderCreated(order.getKey()));
        super.considerUnitEmissionAfterAdd(order, unitOfConsequence);
    }
}
