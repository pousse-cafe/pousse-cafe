package poussecafe.shop.domain.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.shop.domain.Product;
import poussecafe.shop.domain.ProductId;
import poussecafe.spring.mongo.storage.MongoDataAccess;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;

@DataAccessImplementation(
        aggregateRoot = Product.class,
        dataImplementation = ProductData.class,
        storageName = SpringMongoDbStorage.NAME
)
public class ProductDataAccess extends MongoDataAccess<ProductId, ProductData, String> implements poussecafe.shop.domain.ProductDataAccess<ProductData> {

    @Autowired
    private ProductMongoRepository repository;

    @Override
    protected String convertId(ProductId id) {
        return id.stringValue();
    }

    @Override
    protected MongoRepository<ProductData, String> mongoRepository() {
        return repository;
    }

}
