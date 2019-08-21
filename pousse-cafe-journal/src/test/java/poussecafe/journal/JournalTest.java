package poussecafe.journal;

import java.util.List;
import poussecafe.environment.Bundle;
import poussecafe.test.PousseCafeTest;

import static java.util.Collections.singletonList;

public abstract class JournalTest extends PousseCafeTest {

    @Override
    protected List<Bundle> bundles() {
        return singletonList(Journal.configure().defineAndImplementDefault().build());
    }
}
