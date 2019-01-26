package poussecafe.maven;

import java.io.File;

import java.util.Objects;

public class SpringMongoStorageSourceGenerator implements StorageSourceGenerator {

    public static class Builder implements StorageSourceGeneratorBuilder {

        private SpringMongoStorageSourceGenerator generator = new SpringMongoStorageSourceGenerator();

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
        public SpringMongoStorageSourceGenerator build() {
            Objects.requireNonNull(generator.sourceWriter);
            Objects.requireNonNull(generator.aggregateName);
            Objects.requireNonNull(generator.adaptersDirectory);
            return generator;
        }
    }

    private SpringMongoStorageSourceGenerator() {

    }

    private SourceWriter sourceWriter;

    private String aggregateName;

    private File adaptersDirectory;

    @Override
    public void generate() {
        writeSpringMondoDataAccessSource();
        writeDataMongoRepositorySource();
    }

    private void writeSpringMondoDataAccessSource() {
        File outputFile = new File(adaptersDirectory, aggregateName + "MongoDataAccess.java");
        sourceWriter.writeSource(outputFile, "spring_mongo_data_access");
    }

    private void writeDataMongoRepositorySource() {
        File outputFile = new File(adaptersDirectory, aggregateName + "DataMongoRepository.java");
        sourceWriter.writeSource(outputFile, "data_mongo_repository");
    }
}
