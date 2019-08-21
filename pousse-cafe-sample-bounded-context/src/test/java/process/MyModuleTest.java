package process;

import java.util.List;
import poussecafe.environment.Bundle;
import poussecafe.mymodule.MyModule;
import poussecafe.test.PousseCafeTest;

import static java.util.Arrays.asList;

/*
 * Parent class of all tests related to MyModule.
 */
public abstract class MyModuleTest extends PousseCafeTest {

    @Override
    protected List<Bundle> bundles() {
        return asList(MyModule.configure().defineAndImplementDefault().build());
    }
}
