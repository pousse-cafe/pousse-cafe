package poussecafe.journal;

import java.util.List;
import org.junit.Test;
import poussecafe.environment.BoundedContext;
import poussecafe.test.PousseCafeTest;

import static java.util.Collections.singletonList;

public class InternalImplementationTest extends PousseCafeTest {

    @Override
    protected List<BoundedContext> boundedContexts() {
        return singletonList(Journal.configure().defineAndImplementDefault().build());
    }

    @Test
    public void boundedContextConcrete() {

    }
}
