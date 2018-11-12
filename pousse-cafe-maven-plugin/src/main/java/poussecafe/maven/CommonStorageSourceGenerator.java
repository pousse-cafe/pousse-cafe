package poussecafe.maven;

import java.io.File;

import static poussecafe.check.Checks.checkThatValue;

public class CommonStorageSourceGenerator {

    public static class Builder {

        private CommonStorageSourceGenerator generator = new CommonStorageSourceGenerator();

        public Builder sourceWriter(SourceWriter sourceWriter) {
            generator.sourceWriter = sourceWriter;
            return this;
        }

        public Builder aggregateName(String aggregateName) {
            generator.aggregateName = aggregateName;
            return this;
        }

        public Builder adaptersDirectory(File adaptersDirectory) {
            generator.adaptersDirectory = adaptersDirectory;
            return this;
        }

        public CommonStorageSourceGenerator build() {
            checkThatValue(generator.sourceWriter).notNull();
            checkThatValue(generator.aggregateName).notNull();
            checkThatValue(generator.adaptersDirectory).notNull();
            return generator;
        }
    }

    private CommonStorageSourceGenerator() {

    }

    private SourceWriter sourceWriter;

    private String aggregateName;

    private File adaptersDirectory;

    public void generate() {
        writeAggregateRootDataSource();
    }

    private void writeAggregateRootDataSource() {
        File outputFile = new File(adaptersDirectory, aggregateName + "Data.java");
        sourceWriter.writeSource(outputFile, "data");
    }
}
