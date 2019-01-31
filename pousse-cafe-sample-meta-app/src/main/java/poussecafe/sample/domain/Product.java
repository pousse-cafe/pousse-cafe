package poussecafe.sample.domain;

import poussecafe.contextconfigurer.Aggregate;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.sample.domain.Product.Data;

import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.equalTo;
import static poussecafe.check.Predicates.greaterThan;
import static poussecafe.domain.DomainCheckSpecification.value;

@Aggregate(
  factory = ProductFactory.class,
  repository = ProductRepository.class
)
public class Product extends AggregateRoot<ProductKey, Data> {

    void setTotalUnits(int units) {
        data().setTotalUnits(units);
    }

    public int getTotalUnits() {
        return data().getTotalUnits();
    }

    void setAvailableUnits(int units) {
        data().setAvailableUnits(units);
    }

    public int getAvailableUnits() {
        return data().getAvailableUnits();
    }

    public void addUnits(int units) {
        checkThat(value(units).verifies(greaterThan(0).or(equalTo(0))).because("Cannot add negative number of units"));
        data().setAvailableUnits(data().getAvailableUnits() + units);
        data().setTotalUnits(data().getTotalUnits() + units);
    }

    public void placeOrder(OrderDescription description) {
        int unitsAvailable = data().getAvailableUnits();
        if (description.units > unitsAvailable) {
            OrderRejected event = newDomainEvent(OrderRejected.class);
            event.productKey().set(getKey());
            event.description().set(description);
            addDomainEvent(event);
        } else {
            data().setAvailableUnits(unitsAvailable - description.units);

            OrderPlaced event = newDomainEvent(OrderPlaced.class);
            event.productKey().set(getKey());
            event.description().set(description);
            addDomainEvent(event);
        }
    }

    public static interface Data extends EntityData<ProductKey> {

        void setTotalUnits(int units);

        int getTotalUnits();

        void setAvailableUnits(int units);

        int getAvailableUnits();
    }

}
