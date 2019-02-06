package poussecafe.sample.domain;

import poussecafe.contextconfigurer.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;

import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.equalTo;
import static poussecafe.check.Predicates.greaterThan;
import static poussecafe.domain.DomainCheckSpecification.value;

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
        checkThat(value(units).verifies(greaterThan(0).or(equalTo(0))).because("Cannot add negative number of units"));
        attributes().setAvailableUnits(attributes().getAvailableUnits() + units);
        attributes().setTotalUnits(attributes().getTotalUnits() + units);
    }

    public void placeOrder(OrderDescription description) {
        int unitsAvailable = attributes().getAvailableUnits();
        if (description.units > unitsAvailable) {
            OrderRejected event = newDomainEvent(OrderRejected.class);
            event.productKey().valueOf(attributes().key());
            event.description().value(description);
            addDomainEvent(event);
        } else {
            attributes().setAvailableUnits(unitsAvailable - description.units);

            OrderPlaced event = newDomainEvent(OrderPlaced.class);
            event.productKey().valueOf(attributes().key());
            event.description().value(description);
            addDomainEvent(event);
        }
    }

    public static interface Attributes extends EntityAttributes<ProductKey> {

        void setTotalUnits(int units);

        int getTotalUnits();

        void setAvailableUnits(int units);

        int getAvailableUnits();
    }

}
