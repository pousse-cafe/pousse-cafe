package poussecafe.source.generation;

import org.junit.Test;

public class CoreGeneratorTest extends GeneratorTest {

    @Test
    public void newAggregate() { // NOSONAR - add assert - see parent
        givenCoreGenerator();
        givenAggregate();
        whenGeneratingCoreCode();
        thenGeneratedCodeMatchesExpected();
    }

    @Test
    public void updateExistingAggregate() { // NOSONAR - add assert - see parent
        givenCoreGenerator();
        givenAggregate();
        givenExisingCode();
        whenGeneratingCoreCode();
        thenGeneratedCodeMatchesExpected();
    }

    private void givenExisingCode() {
        whenGeneratingCoreCode();
    }

    @Override
    protected String[] packageNameSegments() {
        return new String[] { "poussecafe", "source", "generation", "generated" };
    }
}
