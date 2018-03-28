package poussecafe.sample.app;

import java.util.Set;
import poussecafe.sample.SampleMetaAppBundle;
import poussecafe.sample.domain.Customer;
import poussecafe.sample.domain.Message;
import poussecafe.sample.domain.Order;
import poussecafe.sample.domain.Product;
import poussecafe.sample.domain.mongo.CustomerData;
import poussecafe.sample.domain.mongo.CustomerDataAccess;
import poussecafe.sample.domain.mongo.MessageData;
import poussecafe.sample.domain.mongo.MessageDataAccess;
import poussecafe.sample.domain.mongo.OrderData;
import poussecafe.sample.domain.mongo.OrderDataAccess;
import poussecafe.sample.domain.mongo.ProductData;
import poussecafe.sample.domain.mongo.ProductDataAccess;
import poussecafe.spring.mongo.storage.MongoDbStorage;
import poussecafe.storable.StorableImplementation;

public class SampleMetaAppMongoBundle extends SampleMetaAppBundle {

    @Override
    protected void loadImplementations(Set<StorableImplementation> implementations) {
        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(Customer.class)
                .withDataFactory(CustomerData::new)
                .withDataAccessFactory(CustomerDataAccess::new)
                .withStorage(MongoDbStorage.instance())
                .build());
        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(Message.class)
                .withDataFactory(MessageData::new)
                .withDataAccessFactory(MessageDataAccess::new)
                .withStorage(MongoDbStorage.instance())
                .build());
        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(Product.class)
                .withDataFactory(ProductData::new)
                .withDataAccessFactory(ProductDataAccess::new)
                .withStorage(MongoDbStorage.instance())
                .build());
        implementations.add(new StorableImplementation.Builder()
                .withStorableClass(Order.class)
                .withDataFactory(OrderData::new)
                .withDataAccessFactory(OrderDataAccess::new)
                .withStorage(MongoDbStorage.instance())
                .build());
    }
}
