package poussecafe.source.generation;

import poussecafe.source.generation.internal.InternalStorageAdaptersCodeGenerator;

public class InternalStorageGeneratorTest extends GeneratorTest {

    @Override
    protected void givenStorageGenerator() {
        generator = new InternalStorageAdaptersCodeGenerator.Builder()
                .sourceDirectory(sourceDirectory())
                .codeFormatterProfile(getClass().getResourceAsStream("/CodeFormatterProfileSample.xml"))
                .build();
    }

    private InternalStorageAdaptersCodeGenerator generator;

    @Override
    protected void whenGeneratingStorageCode() {
        generator.generate(aggregate());
    }

    @Override
    protected String[] packageNameSegments() {
        return new String[] { "poussecafe", "source", "generation", "generatedinternal" };
    }
}
