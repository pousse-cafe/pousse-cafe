package poussecafe.sample;

import java.util.Set;
import poussecafe.context.BoundedContext;
import poussecafe.domain.EntityDefinition;
import poussecafe.domain.EntityImplementation;
import poussecafe.domain.Service;
import poussecafe.process.DomainProcess;
import poussecafe.sample.domain.ContentChooser;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.CustomerFactory;
import poussecafe.sample.domain.CustomerRepository;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.MessageFactory;
import poussecafe.sample.domain.MessageRepository;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.OrderFactory;
import poussecafe.sample.domain.OrderRepository;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.ProductFactory;
import poussecafe.sample.domain.ProductRepository;
import poussecafe.sample.domain.memory.CustomerData;
import poussecafe.sample.domain.memory.CustomerDataAccess;
import poussecafe.sample.domain.memory.InMemoryMessageDataAccess;
import poussecafe.sample.domain.memory.MessageData;
import poussecafe.sample.domain.memory.OrderData;
import poussecafe.sample.domain.memory.OrderDataAccess;
import poussecafe.sample.domain.memory.ProductData;
import poussecafe.sample.domain.memory.ProductDataAccess;
import poussecafe.sample.process.CustomerCreation;
import poussecafe.sample.process.Messaging;
import poussecafe.sample.process.OrderPlacement;
import poussecafe.sample.process.ProductManagement;
import poussecafe.storage.memory.InMemoryStorage;

public class SampleMetaAppBoundedContext extends BoundedContext {

    @Override
    protected void loadDefinitions(Set<EntityDefinition> definitions) {
        definitions.add(new EntityDefinition.Builder()
                .withEntityClass(Customer.class)
                .withFactoryClass(CustomerFactory.class)
                .withRepositoryClass(CustomerRepository.class)
                .build());
        definitions.add(new EntityDefinition.Builder()
                .withEntityClass(Message.class)
                .withFactoryClass(MessageFactory.class)
                .withRepositoryClass(MessageRepository.class)
                .build());
        definitions.add(new EntityDefinition.Builder()
                .withEntityClass(Product.class)
                .withFactoryClass(ProductFactory.class)
                .withRepositoryClass(ProductRepository.class)
                .build());
        definitions.add(new EntityDefinition.Builder()
                .withEntityClass(Order.class)
                .withFactoryClass(OrderFactory.class)
                .withRepositoryClass(OrderRepository.class)
                .build());
    }

    @Override
    protected void loadImplementations(Set<EntityImplementation> implementations) {
        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(Customer.class)
                .withDataFactory(CustomerData::new)
                .withDataAccessFactory(CustomerDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());
        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(Message.class)
                .withDataAccessFactory(InMemoryMessageDataAccess::new)
                .withDataFactory(MessageData::new)
                .withStorage(InMemoryStorage.instance())
                .build());
        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(Product.class)
                .withDataFactory(ProductData::new)
                .withDataAccessFactory(ProductDataAccess::new)
                .withStorage(InMemoryStorage.instance())
                .build());
        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(Order.class)
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
    protected void loadServices(Set<Class<? extends Service>> services) {
        services.add(ContentChooser.class);
    }

}
