package poussecafe.journal;

import java.util.List;
import java.util.function.Predicate;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

class HasLog implements Predicate<List<EntryLog>> {

    private EntryLogType type;

    private HasLog(EntryLogType type) {
        setType(type);
    }

    private void setType(EntryLogType type) {
        checkThat(value(type).notNull().because("Log type cannot be null"));
        this.type = type;
    }

    public static HasLog hasLog(EntryLogType type) {
        return new HasLog(type);
    }

    @Override
    public boolean test(List<EntryLog> item) {
        for (EntryLog log : item) {
            if (log.getType() == type) {
                return true;
            }
        }
        return false;
    }

}
