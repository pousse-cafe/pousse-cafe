package poussecafe.data.memory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FloatPropertyTest extends DataPropertyTest {

    @Override
    public void gettingAfterSetIsSameValue() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        implementation.setValue(10.f);
        assertThat(implementation.getValue(), is(10.f));
    }

    @Override
    public void gettingAfterNoSetIsDefault() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        assertThat(implementation.getValue(), is(0.f));
    }

    private static interface TestData {

        void setValue(float v);

        float getValue();
    }
}
