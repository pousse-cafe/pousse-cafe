package poussecafe.storage;

import poussecafe.data.memory.InMemoryDataAccess;
import poussecafe.data.memory.InMemoryDataFactory;
import poussecafe.journal.InMemoryJournalEntryDataAccess;
import poussecafe.journal.JournalEntry;
import poussecafe.storable.StorableData;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableDataFactory;

public class InMemoryStorage extends ServiceCachingStorage {

    public InMemoryStorage() {
        registerDataAccess(JournalEntry.Data.class, new InMemoryJournalEntryDataAccess());
    }

    @Override
    protected ConsequenceEmissionPolicy newConsequenceEmissionPolicy() {
        return new DirectEmissionPolicy();
    }

    @Override
    protected TransactionRunner newTransactionRunner() {
        return new NoTransactionRunner();
    }

    @Override
    protected <K, D extends StorableData<K>> StorableDataAccess<K, D> newDataAccess(Class<D> dataClass) {
        return new InMemoryDataAccess<>(dataClass);
    }

    @Override
    protected <D extends StorableData<?>> StorableDataFactory<D> newDataFactory(Class<D> dataClass) {
        return new InMemoryDataFactory<>(dataClass);
    }

    @Override
    public <D extends StorableData<?>> boolean isStoring(Class<D> dataClass) {
        return true;
    }

}
