package poussecafe.maven;

import java.io.File;

import static poussecafe.check.Checks.checkThatValue;

public class InMemoryStorageSourceGenerator implements StorageSourceGenerator {

    public static class Builder implements StorageSourceGeneratorBuilder {

        private InMemoryStorageSourceGenerator generator = new InMemoryStorageSourceGenerator();

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
        public InMemoryStorageSourceGenerator build() {
            checkThatValue(generator.sourceWriter).notNull();
            checkThatValue(generator.aggregateName).notNull();
            checkThatValue(generator.adaptersDirectory).notNull();
            return generator;
        }
    }

    private InMemoryStorageSourceGenerator() {

    }

    private SourceWriter sourceWriter;

    private String aggregateName;

    private File adaptersDirectory;

    @Override
    public void generate() {
        writeInMemoryDataAccessSource();
    }

    private void writeInMemoryDataAccessSource() {
        File outputFile = new File(adaptersDirectory, aggregateName + "InMemoryDataAccess.java");
        sourceWriter.writeSource(outputFile, "memory_data_access");
    }
}
