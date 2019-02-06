package <adaptersPackage>;

<if(demoAttribute)>
import java.util.List;
<endif>
import org.springframework.data.mongodb.repository.MongoRepository;

public interface <name>DataMongoRepository extends MongoRepository\<<name>Attributes, String> {

<if(demoAttribute)>    List\<<name>Attributes> findByName(String name);<endif>
}
