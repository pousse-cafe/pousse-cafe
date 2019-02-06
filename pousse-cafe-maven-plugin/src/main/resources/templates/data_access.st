package <modelPackage>;

<if(demoAttribute)>import java.util.List;<endif>
import poussecafe.domain.EntityDataAccess;

public interface <name>DataAccess\<D extends <name>.Attributes> extends EntityDataAccess\<<name>Key, D> {

<if(demoAttribute)>
    List\<D> findByName(String name);<endif>
}
