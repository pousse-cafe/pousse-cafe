package <modelPackage>;

import java.util.List;
import poussecafe.domain.Repository;

public class <name>Repository extends Repository\<<name>, <name>Key, <name>.Data> {

    public List\<<name>> findByName(String name) {
        return newEntitiesWithData(dataAccess().findByName(name));
    }

    private <name>DataAccess\<<name>.Data> dataAccess() {
        return (<name>DataAccess\<<name>.Data>) dataAccess;
    }
}
