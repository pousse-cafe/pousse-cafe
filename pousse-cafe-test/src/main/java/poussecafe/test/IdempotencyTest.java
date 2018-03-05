package poussecafe.test;

import poussecafe.exception.SameOperationException;

import static org.junit.Assert.assertTrue;

public abstract class IdempotencyTest extends MetaApplicationTest {

    protected void whenRunningSameOperation() {
        runOperation();
        try {
            runOperation();
            sameOperationExceptionThrown = false;
        } catch(SameOperationException e) {
            sameOperationExceptionThrown = true;
        }
    }

    protected abstract void runOperation();

    private boolean sameOperationExceptionThrown;

    protected void thenSameOperationExceptionThrown() {
        assertTrue(sameOperationExceptionThrown);
    }
}
