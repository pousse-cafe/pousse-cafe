package poussecafe.util;

import java.util.function.Predicate;
import org.junit.Test;
import poussecafe.check.CheckSpecification;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public abstract class CheckSpecificationTest {

    private Object value;

    private Predicate<Object> matcher;

    private String message;

    private CheckSpecification<Object> specification;

    @Test
    public void buildSetsValueMatcherAndMessage() {
        givenValue();
        givenMatcher();
        givenMessage();
        whenBuildingSpecification();
        thenSpecificationHasValueMatcherAndMessage();
    }

    private void givenValue() {
        value = mock(Object.class);
    }

    @SuppressWarnings("unchecked")
    private void givenMatcher() {
        matcher = mock(Predicate.class);
    }

    private void givenMessage() {
        message = "message";
    }

    private void whenBuildingSpecification() {
        specification = buildSpecificationWithValue(value).verifies(matcher).because(message);
    }

    protected abstract CheckSpecification<Object> buildSpecificationWithValue(Object value);

    private void thenSpecificationHasValueMatcherAndMessage() {
        assertThat(specification.getValue(), is(value));
        assertThat(specification.getPredicate(), is((Object) matcher));
        assertThat(specification.getMessage(), is(message));
    }
}
