package poussecafe.sample.domain;

import poussecafe.discovery.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.DomainException;
import poussecafe.domain.EntityAttributes;

@Aggregate(
  factory = ProductFactory.class,
  repository = ProductRepository.class
)
public class Product extends AggregateRoot<ProductKey, Product.Attributes> {

    void setTotalUnits(int units) {
        attributes().setTotalUnits(units);
    }

    public int getTotalUnits() {
        return attributes().getTotalUnits();
    }

    void setAvailableUnits(int units) {
        attributes().setAvailableUnits(units);
    }

    public int getAvailableUnits() {
        return attributes().getAvailableUnits();
    }

    public void addUnits(int units) {
        if(units <= 0) {
            throw new DomainException("Cannot add negative number of units");
        }
        attributes().setAvailableUnits(attributes().getAvailableUnits() + units);
        attributes().setTotalUnits(attributes().getTotalUnits() + units);
    }

    public void placeOrder(OrderDescription description) {
        int unitsAvailable = attributes().getAvailableUnits();
        if (description.units > unitsAvailable) {
            OrderRejected event = newDomainEvent(OrderRejected.class);
            event.productKey().valueOf(attributes().key());
            event.description().value(description);
            emitDomainEvent(event);
        } else {
            attributes().setAvailableUnits(unitsAvailable - description.units);

            OrderPlaced event = newDomainEvent(OrderPlaced.class);
            event.productKey().valueOf(attributes().key());
            event.description().value(description);
            emitDomainEvent(event);
        }
    }

    public static interface Attributes extends EntityAttributes<ProductKey> {

        void setTotalUnits(int units);

        int getTotalUnits();

        void setAvailableUnits(int units);

        int getAvailableUnits();
    }

}
