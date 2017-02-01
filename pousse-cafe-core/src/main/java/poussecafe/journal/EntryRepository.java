package poussecafe.journal;

import java.util.List;
import poussecafe.journal.Entry.Data;
import poussecafe.storable.StorableDataAccess;
import poussecafe.storable.StorableRepository;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;
import static poussecafe.check.Predicates.instanceOf;

public class EntryRepository
extends StorableRepository<Entry, EntryKey, Entry.Data> {

    @Override
    protected Entry newStorable() {
        return new Entry();
    }

    public List<Entry> findByConsequenceId(String consequenceId) {
        return newStorablesWithData(dataAccess().findByConsequenceId(consequenceId));
    }

    @Override
    public void setDataAccess(StorableDataAccess<EntryKey, Data> dataAccess) {
        checkThat(
                value(dataAccess).verifies(instanceOf(EntryDataAccess.class)).because("Data access must be specific"));
        super.setDataAccess(dataAccess);
    }

    protected EntryDataAccess dataAccess() {
        return (EntryDataAccess) dataAccess;
    }

}
