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
public class Order extends AggregateRoot<OrderId, Order.Attributes> {

    /**
     * @process Messaging
     * @process OrderSettlement
     * @event OrderSettled
     */
    @MessageListener(runner = SettleRunner.class)
    public void settle(SettleOrder command) {
        OrderSettled event = newDomainEvent(OrderSettled.class);
        event.orderId().valueOf(attributes().identifier());
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
        event.orderId().valueOf(attributes().identifier());
        emitDomainEvent(event);
    }

    /**
     * @process Messaging
     * @event OrderCreated
     */
    @Override
    public void onAdd() {
        OrderCreated event = newDomainEvent(OrderCreated.class);
        event.orderId().valueOf(attributes().identifier());
        emitDomainEvent(event);
    }

    public static interface Attributes extends EntityAttributes<OrderId> {

        Attribute<Integer> units();
    }

}
