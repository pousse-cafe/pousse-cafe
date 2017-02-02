package poussecafe.sample.domain;

import poussecafe.domain.Factory;
import poussecafe.sample.domain.Payment.PaymentData;

public class PaymentFactory extends Factory<PaymentKey, Payment, PaymentData> {

    public Payment buildReceivedPayment(PaymentKey paymentKey,
            OrderKey orderKey) {
        Payment payment = newAggregateWithKey(paymentKey);
        payment.setOrderKey(orderKey);
        return payment;
    }

    @Override
    protected Payment newAggregate() {
        return new Payment();
    }
}
