package process;

import poussecafe.mymodule.MyModuleBundle;
import poussecafe.runtime.Runtime.Builder;
import poussecafe.test.PousseCafeTest;

/*
 * Parent class of all tests related to MyModule.
 */
public abstract class MyModuleTest extends PousseCafeTest {

    @Override
    protected Builder runtimeBuilder() {
        return super.runtimeBuilder()
                .withBundle(MyModuleBundle.configure().defineAndImplementDefault().build());
    }
}
