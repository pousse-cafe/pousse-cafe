package <adaptersPackage>;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import poussecafe.spring.mongo.storage.MongoDataAccess;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;
import poussecafe.storage.DataAccessImplementation;

import <adaptersPackage>.<name>Data;
import <modelPackage>.<name>;
import <modelPackage>.<name>DataAccess;
import <modelPackage>.<name>Key;

@DataAccessImplementation(
    aggregateRoot = <name>.class,
    dataImplementation = <name>Data.class,
    storageName = SpringMongoDbStorage.NAME
)
public class <name>MongoDataAccess extends MongoDataAccess\<<name>Key, <name>Data, String> implements <name>DataAccess\<<name>Data> {

    @Override
    public List\<<name>Data> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    protected String convertKey(<name>Key key) {
        return key.getValue();
    }

    @Override
    protected <name>DataMongoRepository mongoRepository() {
        return repository;
    }

    @Autowired
    private <name>DataMongoRepository repository;
}
