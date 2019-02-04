package <adaptersPackage>;

<if(demoAttribute)>
import java.util.List;
<endif>
import org.springframework.data.mongodb.repository.MongoRepository;

public interface <name>DataMongoRepository extends MongoRepository\<<name>Data, String> {

<if(demoAttribute)>    List\<<name>Data> findByName(String name);<endif>
}
