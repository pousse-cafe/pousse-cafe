package poussecafe.sample.domain;

import poussecafe.domain.AggregateData;
import poussecafe.domain.AggregateRoot;
import poussecafe.sample.domain.Product.Data;

import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.equalTo;
import static poussecafe.check.Predicates.greaterThan;
import static poussecafe.domain.DomainSpecification.value;

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
            getUnitOfConsequence().addConsequence(new OrderRejected(getData().getKey(), description));
        } else {
            getData().setAvailableUnits(unitsAvailable - description.units);
            getUnitOfConsequence().addConsequence(new OrderPlaced(getData().getKey(), description));
        }
    }

    public static interface Data extends AggregateData<ProductKey> {

        void setTotalUnits(int units);

        int getTotalUnits();

        void setAvailableUnits(int units);

        int getAvailableUnits();
    }

}
