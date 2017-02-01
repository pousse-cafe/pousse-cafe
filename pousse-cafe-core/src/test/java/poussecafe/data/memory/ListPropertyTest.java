package poussecafe.data.memory;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ListPropertyTest extends DataPropertyTest {

    @Override
    public void gettingAfterSetIsSameValue() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        implementation.setValue(value());
        assertThat(implementation.getValue(), equalTo(value()));
    }

    private List<Integer> value() {
        return asList(1, 2, 3);
    }

    @Override
    public void gettingAfterNoSetIsDefault() {
        TestData implementation = InMemoryDataUtils.newDataImplementation(TestData.class);
        assertThat(implementation.getValue(), equalTo(emptyList()));
    }

    private static interface TestData {

        void setValue(List<Integer> x);

        List<Integer> getValue();
    }
}
