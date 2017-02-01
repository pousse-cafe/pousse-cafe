package poussecafe.data.memory;

import poussecafe.data.memory.InMemoryDataUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntPropertyTest extends DataPropertyTest {

    @Override
    public void gettingAfterSetIsSameValue() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        implementation.setValue(10);
        assertThat(implementation.getValue(), is(10));
    }

    @Override
    public void gettingAfterNoSetIsDefault() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        assertThat(implementation.getValue(), is(0));
    }

    private static interface TestData {

        void setValue(int x);

        int getValue();
    }
}
