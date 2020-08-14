package poussecafe.source.generation;

import org.junit.Test;
import poussecafe.source.generation.internal.InternalStorageGenerator;

public class InternalStorageGeneratorTest extends GeneratorTest {

    @Test
    public void newAggregate() { // NOSONAR - add assert - see parent
        givenCoreGenerator();
        givenInternalStorageGenerator();
        givenAggregate();
        whenGeneratingCode();
        thenGeneratedCodeMatchesExpected();
    }

    protected void givenInternalStorageGenerator() {
        generator = new InternalStorageGenerator.Builder()
                .sourceDirectory(sourceDirectory())
                .build();
    }

    private InternalStorageGenerator generator;

    private void whenGeneratingCode() {
        whenGeneratingCoreCode();
        generator.generate(aggregate());
    }

    @Test
    public void updateExistingAggregate() { // NOSONAR - add assert - see parent
        givenCoreGenerator();
        givenInternalStorageGenerator();
        givenAggregate();
        givenExisingCode();
        whenGeneratingCode();
        thenGeneratedCodeMatchesExpected();
    }

    private void givenExisingCode() {
        whenGeneratingCode();
    }

    @Override
    protected String[] packageNameSegments() {
        return new String[] { "poussecafe", "source", "generation", "generatedinternal" };
    }
}
