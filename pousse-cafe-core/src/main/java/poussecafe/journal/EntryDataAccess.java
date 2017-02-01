package poussecafe.journal;

import java.util.List;
import poussecafe.storable.StorableDataAccess;

public interface EntryDataAccess extends StorableDataAccess<EntryKey, Entry.Data> {

    List<Entry.Data> findByConsequenceId(String consequenceId);

}
