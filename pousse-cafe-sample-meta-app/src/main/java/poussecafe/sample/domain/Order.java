package poussecafe.sample.domain;

import poussecafe.context.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.sample.domain.Order.Data;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.greaterThan;

@Aggregate(
  factory = OrderFactory.class,
  repository = OrderRepository.class
)
public class Order extends AggregateRoot<OrderKey, Data> {

    void setUnits(int units) {
        checkThat(value(units).verifies(greaterThan(0)).because("More than 0 units have to be ordered"));
        getData().setUnits(units);
    }

    public void settle() {
        OrderSettled event = newDomainEvent(OrderSettled.class);
        event.orderKey().set(getKey());
        addDomainEvent(event);
    }

    public void ship() {
        OrderReadyForShipping event = newDomainEvent(OrderReadyForShipping.class);
        event.orderKey().set(getKey());
        addDomainEvent(event);
    }

    public static interface Data extends EntityData<OrderKey> {

        void setUnits(int units);

        int getUnits();
    }

}
