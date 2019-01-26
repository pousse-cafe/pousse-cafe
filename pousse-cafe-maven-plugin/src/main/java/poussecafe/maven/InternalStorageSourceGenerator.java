package poussecafe.maven;

import java.io.File;

import java.util.Objects;

public class InternalStorageSourceGenerator implements StorageSourceGenerator {

    public static class Builder implements StorageSourceGeneratorBuilder {

        private InternalStorageSourceGenerator generator = new InternalStorageSourceGenerator();

        @Override
        public Builder sourceWriter(SourceWriter sourceWriter) {
            generator.sourceWriter = sourceWriter;
            return this;
        }

        @Override
        public Builder aggregateName(String aggregateName) {
            generator.aggregateName = aggregateName;
            return this;
        }

        @Override
        public Builder adaptersDirectory(File adaptersDirectory) {
            generator.adaptersDirectory = adaptersDirectory;
            return this;
        }

        @Override
        public InternalStorageSourceGenerator build() {
            Objects.requireNonNull(generator.sourceWriter);
            Objects.requireNonNull(generator.aggregateName);
            Objects.requireNonNull(generator.adaptersDirectory);
            return generator;
        }
    }

    private InternalStorageSourceGenerator() {

    }

    private SourceWriter sourceWriter;

    private String aggregateName;

    private File adaptersDirectory;

    @Override
    public void generate() {
        writeInternalDataAccessSource();
    }

    private void writeInternalDataAccessSource() {
        File outputFile = new File(adaptersDirectory, aggregateName + "InternalDataAccess.java");
        sourceWriter.writeSource(outputFile, "internal_data_access");
    }
}
