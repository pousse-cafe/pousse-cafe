package <adaptersPackage>;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface <name>DataMongoRepository extends MongoRepository\<<name>Data, String> {

    List\<<name>Data> findByName(String name);
}
