package poussecafe.sample.domain;

import poussecafe.domain.AggregateRoot;
import poussecafe.sample.domain.Order.Data;
import poussecafe.storable.ActiveStorableData;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.greaterThan;

public class Order extends AggregateRoot<OrderKey, Data> {

    void setUnits(int units) {
        checkThat(value(units).verifies(greaterThan(0)).because("More than 0 units have to be ordered"));
        getData().setUnits(units);
    }

    public void settle() {
        addDomainEvent(new OrderSettled(getKey()));
    }

    public void ship() {
        addDomainEvent(new OrderReadyForShipping(getKey()));
    }

    public static interface Data extends ActiveStorableData<OrderKey> {

        void setUnits(int units);

        int getUnits();
    }

}
