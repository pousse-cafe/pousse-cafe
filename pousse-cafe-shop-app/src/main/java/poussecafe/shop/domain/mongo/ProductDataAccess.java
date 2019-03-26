package poussecafe.shop.domain.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.shop.domain.ProductKey;
import poussecafe.spring.mongo.storage.MongoDataAccess;

public class ProductDataAccess extends MongoDataAccess<ProductKey, ProductData, String> {

    @Autowired
    private ProductMongoRepository repository;

    @Override
    protected String convertKey(ProductKey key) {
        return key.getValue();
    }

    @Override
    protected MongoRepository<ProductData, String> mongoRepository() {
        return repository;
    }

}
