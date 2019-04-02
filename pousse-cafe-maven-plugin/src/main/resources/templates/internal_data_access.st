package <adaptersPackage>;

<if(demoAttribute)>import java.util.List;<endif>
import poussecafe.discovery.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import <modelPackage>.<name>;
import <modelPackage>.<name>Id;
import <modelPackage>.<name>DataAccess;

<if(demoAttribute)>
import static java.util.stream.Collectors.toList;
<endif>

@DataAccessImplementation(
    aggregateRoot = <name>.class,
    dataImplementation = <name>Attributes.class,
    storageName = InternalStorage.NAME
)
public class <name>InternalDataAccess extends InternalDataAccess\<<name>Id, <name>Attributes> implements <name>DataAccess\<<name>Attributes> {

    public <name>InternalDataAccess() {
        versionField("version");
    }
<if(demoAttribute)>

    @Override
    public List\<<name>Attributes> findByName(String name) {
        return findAll().stream()
                .filter(data -> data.name().valueEquals(name))
                .collect(toList());
    }
<endif>
}
