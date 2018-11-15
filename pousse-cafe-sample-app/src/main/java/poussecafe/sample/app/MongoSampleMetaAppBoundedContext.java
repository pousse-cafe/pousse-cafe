package poussecafe.sample.app;

import java.util.Set;
import poussecafe.domain.EntityImplementation;
import poussecafe.sample.SampleMetaAppBoundedContext;
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
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;

public class MongoSampleMetaAppBoundedContext extends SampleMetaAppBoundedContext {

    @Override
    protected void loadEntityImplementations(Set<EntityImplementation> implementations) {
        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(Customer.class)
                .withDataFactory(CustomerData::new)
                .withDataAccessFactory(CustomerDataAccess::new)
                .withStorage(SpringMongoDbStorage.instance())
                .build());
        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(Message.class)
                .withDataFactory(MessageData::new)
                .withDataAccessFactory(MessageDataAccess::new)
                .withStorage(SpringMongoDbStorage.instance())
                .build());
        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(Product.class)
                .withDataFactory(ProductData::new)
                .withDataAccessFactory(ProductDataAccess::new)
                .withStorage(SpringMongoDbStorage.instance())
                .build());
        implementations.add(new EntityImplementation.Builder()
                .withEntityClass(Order.class)
                .withDataFactory(OrderData::new)
                .withDataAccessFactory(OrderDataAccess::new)
                .withStorage(SpringMongoDbStorage.instance())
                .build());
    }
}
