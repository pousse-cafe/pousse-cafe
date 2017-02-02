package poussecafe.sample.domain;

import poussecafe.domain.AggregateData;
import poussecafe.domain.AggregateRoot;
import poussecafe.sample.domain.Payment.PaymentData;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

public class Payment extends AggregateRoot<PaymentKey, PaymentData> {

    void setOrderKey(OrderKey orderKey) {
        checkThat(value(orderKey).notNull().because("Order key cannot be null"));
        getData().setOrderKey(orderKey);
    }

    public static interface PaymentData extends AggregateData<PaymentKey> {

        void setOrderKey(OrderKey orderKey);
    }

}
