package poussecafe.source.generation.internal;

import java.nio.file.Path;
import poussecafe.source.generation.ComilationUnitEditor;
import poussecafe.source.generation.StorageGenerator;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

public class InternalStorageGenerator extends StorageGenerator {

    @Override
    protected void updateDefaultAttributesImplementation(Aggregate aggregate, ComilationUnitEditor compilationUnitEditor) {
        var aggregateFactoryEditor = new InternalAttributesImplementationEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateFactoryEditor.edit();
    }

    @Override
    protected void addDataAccessImplementation(Aggregate aggregate, ComilationUnitEditor compilationUnitEditor) {
        var aggregateFactoryEditor = new InternalDataAccessImplementationEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateFactoryEditor.edit();
    }

    @Override
    protected String storageName() {
        return INTERNAL_STORAGE_NAME;
    }

    public static final String INTERNAL_STORAGE_NAME = "Internal";

    public static class Builder {

        private InternalStorageGenerator generator = new InternalStorageGenerator();

        public InternalStorageGenerator build() {
            requireNonNull(generator.sourceDirectory);
            return generator;
        }

        public Builder sourceDirectory(Path sourceDirectory) {
            generator.sourceDirectory = sourceDirectory;
            return this;
        }
    }

    private InternalStorageGenerator() {

    }
}
