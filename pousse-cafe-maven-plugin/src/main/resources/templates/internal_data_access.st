package <adaptersPackage>;

<if(demoAttribute)>import java.util.List;<endif>
import poussecafe.contextconfigurer.DataAccessImplementation;
import poussecafe.storage.internal.InternalDataAccess;
import poussecafe.storage.internal.InternalStorage;

import <modelPackage>.<name>;
import <modelPackage>.<name>Key;
import <modelPackage>.<name>DataAccess;

<if(demoAttribute)>
import static java.util.stream.Collectors.toList;
<endif>

@DataAccessImplementation(
    aggregateRoot = <name>.class,
    dataImplementation = <name>Data.class,
    storageName = InternalStorage.NAME
)
public class <name>InternalDataAccess extends InternalDataAccess\<<name>Key, <name>Data> implements <name>DataAccess\<<name>Data> {

    public <name>InternalDataAccess() {
        versionField("version");
    }
<if(demoAttribute)>

    @Override
    public List\<<name>Data> findByName(String name) {
        return findAll().stream()
                .filter(data -> data.name().get().equals(name))
                .collect(toList());
    }
<endif>
}
