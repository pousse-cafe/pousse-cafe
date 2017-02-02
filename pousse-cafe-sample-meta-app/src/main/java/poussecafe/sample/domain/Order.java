package poussecafe.sample.domain;

import java.util.Objects;
import poussecafe.domain.AggregateData;
import poussecafe.domain.AggregateRoot;
import poussecafe.sample.domain.Order.OrderData;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.greaterThan;

public class Order extends AggregateRoot<OrderKey, OrderData> {

    void setProductKey(ProductKey productKey) {
        checkThat(value(productKey).notNull().because("Product key cannot be null"));
        getData().setProductKey(productKey);
    }

    void setUnits(int units) {
        checkThat(value(units).verifies(greaterThan(0)).because("Ordered units cannot be lower than 0"));
        getData().setUnits(units);
    }

    public void setPayment(PaymentKey paymentKey) {
        checkThat(value(paymentKey).notNull().because("Payment cannot be null"));
        checkThat(value(getData().getPaymentKey()).verifies(Objects::isNull)
                .because("Order has already a payment"));

        getData().setPaymentKey(paymentKey);
        addDomainEvent(new OrderSettled(getData().getKey()));
    }

    public static interface OrderData extends AggregateData<OrderKey> {

        void setProductKey(ProductKey productKey);

        ProductKey getProductKey();

        void setUnits(int units);

        int getUnits();

        void setPaymentKey(PaymentKey paymentKey);

        PaymentKey getPaymentKey();
    }

}
