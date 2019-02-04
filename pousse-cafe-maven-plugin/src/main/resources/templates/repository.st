package <modelPackage>;

<if(demoAttribute)>import java.util.List;<endif>
import poussecafe.domain.Repository;

public class <name>Repository extends Repository\<<name>, <name>Key, <name>.Data> {

<if(demoAttribute)>
    public List\<<name>> findByName(String name) {
        return wrap(dataAccess().findByName(name));
    }
 <endif>

    @Override
    public <name>DataAccess\<<name>.Data> dataAccess() {
        return (<name>DataAccess\<<name>.Data>) super.dataAccess();
    }
}
