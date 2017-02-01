package poussecafe.data.memory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LongPropertyTest extends DataPropertyTest {

    @Override
    public void gettingAfterSetIsSameValue() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        implementation.setValue(10L);
        assertThat(implementation.getValue(), is(10L));
    }

    @Override
    public void gettingAfterNoSetIsDefault() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        assertThat(implementation.getValue(), is(0L));
    }

    private static interface TestData {

        void setValue(long v);

        long getValue();
    }
}
