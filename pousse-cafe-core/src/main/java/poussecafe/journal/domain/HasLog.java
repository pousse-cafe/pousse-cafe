package poussecafe.journal.domain;

import java.util.List;
import java.util.function.Predicate;

import static poussecafe.check.AssertionSpecification.value;
import static poussecafe.check.Checks.checkThat;

class HasLog implements Predicate<List<JournalEntryLog>> {

    private JournalEntryLogType type;

    private HasLog(JournalEntryLogType type) {
        setType(type);
    }

    private void setType(JournalEntryLogType type) {
        checkThat(value(type).notNull().because("Log type cannot be null"));
        this.type = type;
    }

    public static HasLog hasLog(JournalEntryLogType type) {
        return new HasLog(type);
    }

    @Override
    public boolean test(List<JournalEntryLog> item) {
        for (JournalEntryLog log : item) {
            if (log.getType() == type) {
                return true;
            }
        }
        return false;
    }

}
