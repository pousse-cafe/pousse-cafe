package <modelPackage>;

import java.util.List;
import poussecafe.domain.EntityDataAccess;

public interface <name>DataAccess\<D extends <name>.Data> extends EntityDataAccess\<<name>Key, D> {

    List\<D> findByName(String name);
}
