package poussecafe.sample.domain;

import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityData;
import poussecafe.sample.domain.Product.Data;

import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.equalTo;
import static poussecafe.check.Predicates.greaterThan;
import static poussecafe.domain.DomainCheckSpecification.value;

public class Product extends AggregateRoot<ProductKey, Data> {

    void setTotalUnits(int units) {
        getData().setTotalUnits(units);
    }

    public int getTotalUnits() {
        return getData().getTotalUnits();
    }

    void setAvailableUnits(int units) {
        getData().setAvailableUnits(units);
    }

    public int getAvailableUnits() {
        return getData().getAvailableUnits();
    }

    public void addUnits(int units) {
        checkThat(value(units).verifies(greaterThan(0).or(equalTo(0))).because("Cannot add negative number of units"));
        getData().setAvailableUnits(getData().getAvailableUnits() + units);
        getData().setTotalUnits(getData().getTotalUnits() + units);
    }

    public void placeOrder(OrderDescription description) {
        int unitsAvailable = getData().getAvailableUnits();
        if (description.units > unitsAvailable) {
            OrderRejected event = newDomainEvent(OrderRejected.class);
            event.productKey().set(getKey());
            event.description().set(description);
            addDomainEvent(event);
        } else {
            getData().setAvailableUnits(unitsAvailable - description.units);

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
