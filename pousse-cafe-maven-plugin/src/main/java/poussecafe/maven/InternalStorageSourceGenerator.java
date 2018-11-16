package poussecafe.maven;

import java.io.File;

import static poussecafe.check.Checks.checkThatValue;

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
            checkThatValue(generator.sourceWriter).notNull();
            checkThatValue(generator.aggregateName).notNull();
            checkThatValue(generator.adaptersDirectory).notNull();
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
