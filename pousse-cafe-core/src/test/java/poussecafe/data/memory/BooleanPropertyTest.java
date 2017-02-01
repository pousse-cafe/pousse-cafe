package poussecafe.data.memory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BooleanPropertyTest extends DataPropertyTest {

    @Override
    public void gettingAfterSetIsSameValue() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        implementation.setValue(true);
        assertThat(implementation.getValue(), is(true));
    }

    @Override
    public void gettingAfterNoSetIsDefault() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        assertThat(implementation.getValue(), is(false));
    }

    private static interface TestData {

        void setValue(boolean v);

        boolean getValue();
    }
}
