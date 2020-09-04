package poussecafe.source.generation.internal;

import java.io.InputStream;
import java.nio.file.Path;
import poussecafe.source.generation.StorageAdaptersCodeGenerator;
import poussecafe.source.generation.tools.CompilationUnitEditor;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

public class InternalStorageAdaptersCodeGenerator extends StorageAdaptersCodeGenerator {

    @Override
    protected void updateDefaultAttributesImplementation(Aggregate aggregate, CompilationUnitEditor compilationUnitEditor) {
        var aggregateFactoryEditor = new InternalAttributesImplementationEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateFactoryEditor.edit();
    }

    @Override
    protected void addDataAccessImplementation(Aggregate aggregate, CompilationUnitEditor compilationUnitEditor) {
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

        private InternalStorageAdaptersCodeGenerator generator = new InternalStorageAdaptersCodeGenerator();

        public InternalStorageAdaptersCodeGenerator build() {
            requireNonNull(generator.sourceDirectory);
            requireNonNull(generator.formatterOptions);
            return generator;
        }

        public Builder sourceDirectory(Path sourceDirectory) {
            generator.sourceDirectory = sourceDirectory;
            return this;
        }

        public Builder codeFormatterProfile(Path profile) {
            generator.loadProfileFromFile(profile);
            return this;
        }

        public Builder codeFormatterProfile(InputStream profile) {
            generator.loadProfileFromFile(profile);
            return this;
        }
    }

    private InternalStorageAdaptersCodeGenerator() {

    }
}
