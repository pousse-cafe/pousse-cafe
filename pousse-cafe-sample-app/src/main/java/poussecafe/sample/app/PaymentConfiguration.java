package poussecafe.sample.app;

import org.springframework.context.annotation.Configuration;
import poussecafe.configuration.AggregateConfiguration;
import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryStorableDataFactory;
import poussecafe.sample.domain.Payment;
import poussecafe.sample.domain.Payment.PaymentData;
import poussecafe.sample.domain.PaymentFactory;
import poussecafe.sample.domain.PaymentKey;
import poussecafe.sample.domain.PaymentRepository;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataFactory;

@Configuration
public class PaymentConfiguration
extends AggregateConfiguration<PaymentKey, Payment, PaymentData, PaymentFactory, PaymentRepository> {

    public PaymentConfiguration() {
        super(Payment.class, PaymentFactory.class, PaymentRepository.class);
    }

    @Override
    protected StorableDataFactory<PaymentData> aggregateDataFactory() {
        return new InMemoryStorableDataFactory<>(PaymentData.class);
    }

    @Override
    protected StorableDataAccess<PaymentKey, PaymentData> dataAccess() {
        return new InMemoryDataAccess<>(PaymentData.class);
    }

}
