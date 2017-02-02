package poussecafe.sample.domain;

import poussecafe.domain.Repository;
import poussecafe.sample.domain.Payment.PaymentData;

public class PaymentRepository extends Repository<Payment, PaymentKey, PaymentData> {

    @Override
    protected Payment newAggregate() {
        return new Payment();
    }

}
