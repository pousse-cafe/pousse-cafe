package process;

import java.util.List;
import poussecafe.environment.BoundedContext;
import poussecafe.myboundedcontext.MyBoundedContext;
import poussecafe.test.PousseCafeTest;

import static java.util.Arrays.asList;

/*
 * Parent class of all tests related to MyBoundedContext.
 */
public abstract class MyBoundedContextTest extends PousseCafeTest {

    @Override
    protected List<BoundedContext> boundedContexts() {
        return asList(MyBoundedContext.configure().defineAndImplementDefault().build());
    }
}
