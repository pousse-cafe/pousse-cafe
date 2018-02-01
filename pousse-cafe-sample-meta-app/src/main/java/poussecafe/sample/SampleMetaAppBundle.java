package poussecafe.sample;

import java.util.Set;
import poussecafe.context.MetaApplicationBundle;
import poussecafe.sample.domain.ContentChooser;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerFactory;
import poussecafe.sample.domain.CustomerRepository;
import poussecafe.sample.domain.InMemoryMessageDataAccess;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.MessageFactory;
import poussecafe.sample.domain.MessageRepository;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderFactory;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductFactory;
import poussecafe.sample.domain.ProductRepository;
import poussecafe.sample.domain.data.CustomerData;
import poussecafe.sample.domain.data.CustomerDataAccess;
import poussecafe.sample.domain.data.MessageData;
import poussecafe.sample.domain.data.OrderData;
import poussecafe.sample.domain.data.OrderDataAccess;
import poussecafe.sample.domain.data.ProductData;
import poussecafe.sample.domain.data.ProductDataAccess;
import poussecafe.sample.process.CustomerCreation;
import poussecafe.sample.process.Messaging;
import poussecafe.sample.process.OrderPlacement;
import poussecafe.sample.process.ProductManagement;
import poussecafe.service.DomainProcess;
import poussecafe.storable.StorableDefinition;
import poussecafe.storable.StorableImplementation;
import poussecafe.storage.InMemoryStorage;

public class SampleMetaAppBundle extends MetaApplicationBundle {

    @Override
    protected void loadDefinitions(Set<StorableDefinition> definitions) {
        definitions.add(new StorableDefinition.Builder()
                .withStorableClass(Customer.class)
                .withFactoryClass(CustomerFactory.class)
                .withRepositoryClass(CustomerRepository.class)
                .build());
        definitions.add(new StorableDefinition.Builder()
                .withStorableClass(Message.class)
                .withFactoryClass(MessageFactory.class)
                .withRepositoryClass(MessageRepository.class)
                .build());
        definitions.add(new StorableDefinition.Builder()
                .withStorableClass(Product.class)
                .withFactoryClass(ProductFactory.class)
                .withRepositoryClass(ProductRepository.class)
                .build());
        definitions.add(new StorableDefinition.Builder()
                .withStorableClass(Order.class)
                .withFactoryClass(OrderFactory.class)
                .withRepositoryClass(OrderRepository.class)
                .build());
    }

    @Override
    protected void loadImplementations(Set<StorableImplementation> implementations) {
        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(Customer.class)
                .withDataFactory(CustomerData::new)
                .withDataAccessFactory(CustomerDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());
        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(Message.class)
                .withDataAccessFactory(InMemoryMessageDataAccess::new)
                .withDataFactory(MessageData::new)
                .withStorage(InMemoryStorage.instance())
                .build());
        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(Product.class)
                .withDataFactory(ProductData::new)
                .withDataAccessFactory(ProductDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());
        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(Order.class)
                .withDataFactory(OrderData::new)
                .withDataAccessFactory(OrderDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());
    }

    @Override
    protected void loadProcesses(Set<Class<? extends DomainProcess>> processes) {
        processes.add(ProductManagement.class);
        processes.add(Messaging.class);
        processes.add(CustomerCreation.class);
        processes.add(OrderPlacement.class);
    }

    @Override
    protected void loadServices(Set<Class<?>> services) {
        services.add(ContentChooser.class);
    }

}
