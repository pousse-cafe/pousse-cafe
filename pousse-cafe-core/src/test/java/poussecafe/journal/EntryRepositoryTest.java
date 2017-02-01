package poussecafe.journal;

import poussecafe.journal.Entry.Data;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableRepository;
import poussecafe.storable.StorableRepositoryTest;

import static org.mockito.Mockito.mock;

public class EntryRepositoryTest extends StorableRepositoryTest<EntryKey, Entry, Entry.Data> {

    @Override
    protected Class<Data> dataClass() {
        return Entry.Data.class;
    }

    @Override
    protected StorableDataAccess<EntryKey, Data> mockStorableDataAccess() {
        return mock(EntryDataAccess.class);
    }

    @Override
    protected StorableRepository<Entry, EntryKey, Data> buildRepository() {
        return new EntryRepository();
    }

    @Override
    protected EntryKey buildKey() {
        return new EntryKey("consequenceId", "listenerId");
    }

    @Override
    protected Entry mockStorable() {
        return mock(Entry.class);
    }

}
