package poussecafe.data.memory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ReferencePropertyTest extends DataPropertyTest {

    @Override
    public void gettingAfterSetIsSameValue() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        implementation.setValue(10);
        assertThat(implementation.getValue(), is(10));
    }

    @Override
    public void gettingAfterNoSetIsDefault() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        assertThat(implementation.getValue(), nullValue());
    }

    private static interface TestData {

        void setValue(Object x);

        Object getValue();
    }
}
