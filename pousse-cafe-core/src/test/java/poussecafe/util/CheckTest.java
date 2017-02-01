package poussecafe.util;

import java.util.function.Predicate;
import org.junit.Test;
import poussecafe.check.Check;
import poussecafe.check.CheckSpecification;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CheckTest {

    private CheckSpecification<String> specification;

    @Test
    public void matchingValueDoesNothing() {
        givenMatchingValue();
        whenChecking();
        thenNothingHappens();
    }

    private void givenMatchingValue() {
        givenValue(true);
    }

    @SuppressWarnings("unchecked")
    private void givenValue(boolean matching) {
        specification = mock(CheckSpecification.class);
        @SuppressWarnings("rawtypes")
        Predicate matcher = mock(Predicate.class);
        String value = "";
        when(specification.getValue()).thenReturn(value);
        when(matcher.test(value)).thenReturn(matching);
        when(specification.getPredicate()).thenReturn(matcher);
        String message = "message";
        when(specification.getMessage()).thenReturn(message);
    }

    private void whenChecking() {
        new Check<>(specification).run();
    }

    private void thenNothingHappens() {
        verify(specification, never()).otherwise();
    }

    @Test
    public void nonMatchingValueRunsOtherwise() {
        givenNonMatchingValue();
        whenChecking();
        thenOtherwiseIsExecuted();
    }

    private void givenNonMatchingValue() {
        givenValue(false);
    }

    private void thenOtherwiseIsExecuted() {
        verify(specification).otherwise();
    }
}
