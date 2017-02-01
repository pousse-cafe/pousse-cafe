package poussecafe.data.memory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BytePropertyTest extends DataPropertyTest {

    @Override
    public void gettingAfterSetIsSameValue() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        implementation.setValue((byte) 10);
        assertThat(implementation.getValue(), is((byte) 10));
    }

    @Override
    public void gettingAfterNoSetIsDefault() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        assertThat(implementation.getValue(), is((byte) 0));
    }

    private static interface TestData {

        void setValue(byte v);

        byte getValue();
    }
}
