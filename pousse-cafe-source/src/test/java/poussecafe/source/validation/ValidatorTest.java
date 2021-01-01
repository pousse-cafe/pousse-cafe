package poussecafe.source.validation;

import java.util.function.Predicate;
import poussecafe.source.analysis.ClassLoaderClassResolver;

import static org.junit.Assert.assertTrue;

public abstract class ValidatorTest {

    protected void givenValidator() {
        validator = new Validator(new ClassLoaderClassResolver());
    }

    protected Validator validator;

    protected void whenValidating() {
        validator.validate();
        result = validator.result();
    }

    protected ValidationResult result;

    protected void thenNoMessage() {
        assertTrue(result.messages().isEmpty());
    }

    protected void thenAtLeast(Predicate<ValidationMessage> predicate) {
        assertTrue(result.messages().stream().anyMatch(predicate));
    }
}
