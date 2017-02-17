package poussecafe.journal;

import poussecafe.journal.JournalEntry.Data;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableRepository;
import poussecafe.storable.StorableRepositoryTest;

import static org.mockito.Mockito.mock;

public class JournalEntryRepositoryTest
extends StorableRepositoryTest<JournalEntryKey, JournalEntry, JournalEntry.Data> {

    @Override
    protected Class<Data> dataClass() {
        return JournalEntry.Data.class;
    }

    @Override
    protected StorableDataAccess<JournalEntryKey, Data> mockStorableDataAccess() {
        return mock(JournalEntryDataAccess.class);
    }

    @Override
    protected StorableRepository<JournalEntry, JournalEntryKey, Data> buildRepository() {
        return new JournalEntryRepository();
    }

    @Override
    protected JournalEntryKey buildKey() {
        return new JournalEntryKey("consequenceId", "listenerId");
    }

    @Override
    protected JournalEntry mockStorable() {
        return mock(JournalEntry.class);
    }

}
