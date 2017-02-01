package poussecafe.util;

import poussecafe.check.AssertionSpecification;
import poussecafe.check.CheckSpecification;

public class AssertionSpecificationTest extends CheckSpecificationTest {

    @Override
    protected CheckSpecification<Object> buildSpecificationWithValue(Object value) {
        return AssertionSpecification.value(value);
    }

}
