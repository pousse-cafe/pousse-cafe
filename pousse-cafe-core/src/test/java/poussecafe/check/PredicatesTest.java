package poussecafe.check;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PredicatesTest {

    @Test
    public void lessThanPredicate() {
        assertTrue(Predicates.lessThan(10).test(5));
    }
}
