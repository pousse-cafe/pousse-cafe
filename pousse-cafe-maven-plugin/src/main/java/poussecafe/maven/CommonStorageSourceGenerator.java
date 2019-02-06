package poussecafe.maven;

import java.io.File;
import java.util.Objects;

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
            Objects.requireNonNull(generator.sourceWriter);
            Objects.requireNonNull(generator.aggregateName);
            Objects.requireNonNull(generator.adaptersDirectory);
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
        File outputFile = new File(adaptersDirectory, aggregateName + "Attributes.java");
        sourceWriter.writeSource(outputFile, "data");
    }
}
