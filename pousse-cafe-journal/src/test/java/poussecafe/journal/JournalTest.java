package poussecafe.journal;

import java.util.List;
import poussecafe.environment.BoundedContext;
import poussecafe.test.PousseCafeTest;

import static java.util.Collections.singletonList;

public abstract class JournalTest extends PousseCafeTest {

    @Override
    protected List<BoundedContext> boundedContexts() {
        return singletonList(Journal.configure().defineAndImplementDefault().build());
    }
}
