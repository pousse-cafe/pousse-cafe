package poussecafe.sample.domain;

import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = OrderFactory.class,
  repository = OrderRepository.class
)
public class Order extends AggregateRoot<OrderKey, Order.Attributes> {

    void setUnits(int units) {
        if(units <= 0) {
            throw new IllegalArgumentException("More than 0 units have to be ordered");
        }
        attributes().setUnits(units);
    }

    public void settle() {
        OrderSettled event = newDomainEvent(OrderSettled.class);
        event.orderKey().valueOf(attributes().key());
        emitDomainEvent(event);
    }

    public void ship() {
        OrderReadyForShipping event = newDomainEvent(OrderReadyForShipping.class);
        event.orderKey().valueOf(attributes().key());
        emitDomainEvent(event);
    }

    @Override
    public void onAdd() {
        OrderCreated event = newDomainEvent(OrderCreated.class);
        event.orderKey().valueOf(attributes().key());
        emitDomainEvent(event);
    }

    public static interface Attributes extends EntityAttributes<OrderKey> {

        void setUnits(int units);

        int getUnits();
    }

}
