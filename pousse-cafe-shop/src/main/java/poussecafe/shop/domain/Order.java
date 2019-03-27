package poussecafe.shop.domain;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.shop.command.SettleOrder;
import poussecafe.shop.command.ShipOrder;

@Aggregate(
  factory = OrderFactory.class,
  repository = OrderRepository.class
)
public class Order extends AggregateRoot<OrderKey, Order.Attributes> {

    /**
     * @process Messaging
     * @process OrderSettlement
     * @event OrderSettled
     */
    @MessageListener(runner = SettleRunner.class)
    public void settle(SettleOrder command) {
        OrderSettled event = newDomainEvent(OrderSettled.class);
        event.orderKey().valueOf(attributes().key());
        emitDomainEvent(event);
    }

    /**
     * @process OrderShippment
     * @process Messaging
     * @event OrderReadyForShipping
     */
    @MessageListener(runner = ShipOrderRunner.class)
    public void ship(ShipOrder command) {
        OrderReadyForShipping event = newDomainEvent(OrderReadyForShipping.class);
        event.orderKey().valueOf(attributes().key());
        emitDomainEvent(event);
    }

    /**
     * @process Messaging
     * @event OrderCreated
     */
    @Override
    public void onAdd() {
        OrderCreated event = newDomainEvent(OrderCreated.class);
        event.orderKey().valueOf(attributes().key());
        emitDomainEvent(event);
    }

    public static interface Attributes extends EntityAttributes<OrderKey> {

        Attribute<Integer> units();
    }

}
