package poussecafe.shop.domain;

import poussecafe.attribute.Attribute;
import poussecafe.discovery.Aggregate;
import poussecafe.discovery.MessageListener;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainException;
import poussecafe.domain.EntityAttributes;
import poussecafe.shop.command.AddUnits;
import poussecafe.shop.command.PlaceOrder;

@Aggregate(
  factory = ProductFactory.class,
  repository = ProductRepository.class
)
public class Product extends AggregateRoot<ProductKey, Product.Attributes> {

    /**
     * @process ProductManagement
     */
    @MessageListener(runner = AddUnitsRunner.class)
    public void addUnits(AddUnits command) {
        int units = command.units().value();
        if(units <= 0) {
            throw new DomainException("Cannot add negative number of units");
        }
        attributes().availableUnits().value(attributes().availableUnits().value() + units);
        attributes().totalUnits().value(attributes().totalUnits().value() + units);
    }

    /**
     * @process OrderPlacement
     * @event OrderRejected
     * @event OrderPlaced
     */
    @MessageListener(runner = PlaceOrderRunner.class)
    public void placeOrder(PlaceOrder command) {
        int unitsAvailable = attributes().availableUnits().value();
        OrderDescription description = command.description().value();
        if (description.units() > unitsAvailable) {
            OrderRejected event = newDomainEvent(OrderRejected.class);
            event.productKey().valueOf(attributes().key());
            event.description().value(description);
            emitDomainEvent(event);
        } else {
            attributes().availableUnits().value(unitsAvailable - description.units());

            OrderPlaced event = newDomainEvent(OrderPlaced.class);
            event.productKey().valueOf(attributes().key());
            event.description().value(description);
            emitDomainEvent(event);
        }
    }

    public static interface Attributes extends EntityAttributes<ProductKey> {

        Attribute<Integer> totalUnits();

        Attribute<Integer> availableUnits();
    }

}
