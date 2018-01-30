package poussecafe.sample.domain;

import poussecafe.domain.AggregateRoot;
import poussecafe.sample.domain.Order.Data;
import poussecafe.storable.Property;
import poussecafe.storable.StorableData;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.greaterThan;

public class Order extends AggregateRoot<OrderKey, Data> {

    @Override
    public OrderKey getKey() {
        return new OrderKey(new ProductKey(getData().productKey().get()),
                new CustomerKey(getData().customerKey().get()), getData().reference().get());
    }

    @Override
    public void setKey(OrderKey key) {
        getData().productKey().set(key.getProductKey().getValue());
        getData().customerKey().set(key.getCustomerKey().getValue());
        getData().reference().set(key.getReference());
    }

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

    public static interface Data extends StorableData {

        Property<String> productKey();

        Property<String> customerKey();

        Property<String> reference();

        void setUnits(int units);

        int getUnits();
    }

}
