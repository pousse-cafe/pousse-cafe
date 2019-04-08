package poussecafe.shop.domain.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import poussecafe.shop.domain.ProductId;
import poussecafe.spring.mongo.storage.MongoDataAccess;

public class ProductDataAccess extends MongoDataAccess<ProductId, ProductData, String> {

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
