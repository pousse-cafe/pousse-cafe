package <modelPackage>;

<if(demoAttribute)>import java.util.List;<endif>
import poussecafe.domain.Repository;

public class <name>Repository extends Repository\<<name>, <name>Key, <name>.Attributes> {

<if(demoAttribute)>
    public List\<<name>> findByName(String name) {
        return wrap(dataAccess().findByName(name));
    }
 <endif>

    @Override
    public <name>DataAccess\<<name>.Attributes> dataAccess() {
        return (<name>DataAccess\<<name>.Attributes>) super.dataAccess();
    }
}
