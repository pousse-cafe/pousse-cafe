package poussecafe.data.memory;

import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataAccessTest;

public class InMemoryDataAccessTest extends StorableDataAccessTest {

    @Override
    protected StorableDataAccess<String, Data> newDataAccess() {
        return new InMemoryDataAccess<>(Data.class);
    }

    @Override
    protected Data newData() {
        return InMemoryDataUtils.newDataImplementation(Data.class);
    }

}
