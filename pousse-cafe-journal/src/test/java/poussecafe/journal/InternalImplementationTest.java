package poussecafe.journal;

import java.util.List;
import org.junit.Test;
import poussecafe.context.BoundedContext;
import poussecafe.test.MetaApplicationTest;

import static java.util.Collections.singletonList;

public class InternalImplementationTest extends MetaApplicationTest {

    @Override
    protected List<BoundedContext> testBundle() {
        return singletonList(Journal.configure().defineAndImplementDefault().build());
    }

    @Test
    public void boundedContextConcrete() {

    }
}
