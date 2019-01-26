package poussecafe.maven;

import java.io.File;

import java.util.Objects;

public class ModelSourceGenerator {

    public static class Builder {

        private ModelSourceGenerator generator = new ModelSourceGenerator();

        public Builder sourceWriter(SourceWriter sourceWriter) {
            generator.sourceWriter = sourceWriter;
            return this;
        }

        public Builder aggregateName(String aggregateName) {
            generator.aggregateName = aggregateName;
            return this;
        }

        public Builder modelDirectory(File modelDirectory) {
            generator.modelDirectory = modelDirectory;
            return this;
        }

        public ModelSourceGenerator build() {
            Objects.requireNonNull(generator.sourceWriter);
            Objects.requireNonNull(generator.aggregateName);
            Objects.requireNonNull(generator.modelDirectory);
            return generator;
        }
    }

    private ModelSourceGenerator() {

    }

    private SourceWriter sourceWriter;

    private String aggregateName;

    private File modelDirectory;

    public void generate() {
        writeAggregateRootKeySource();
        writeAggregateRootSource();
        writeDataAccessSource();
        writeRepositorySource();
        writeFactorySource();
    }

    private void writeAggregateRootKeySource() {
        File outputFile = new File(modelDirectory, aggregateName + "Key.java");
        sourceWriter.writeSource(outputFile, "key");
    }

    private void writeAggregateRootSource() {
        File outputFile = new File(modelDirectory, aggregateName + ".java");
        sourceWriter.writeSource(outputFile, "aggregate_root");
    }

    private void writeDataAccessSource() {
        File outputFile = new File(modelDirectory, aggregateName + "DataAccess.java");
        sourceWriter.writeSource(outputFile, "data_access");
    }

    private void writeRepositorySource() {
        File outputFile = new File(modelDirectory, aggregateName + "Repository.java");
        sourceWriter.writeSource(outputFile, "repository");
    }

    private void writeFactorySource() {
        File outputFile = new File(modelDirectory, aggregateName + "Factory.java");
        sourceWriter.writeSource(outputFile, "factory");
    }
}
