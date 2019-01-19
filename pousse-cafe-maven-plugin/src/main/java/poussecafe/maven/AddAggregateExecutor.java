package poussecafe.maven;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.maven.plugin.MojoExecutionException;
import poussecafe.exception.PousseCafeException;
import poussecafe.spring.mongo.storage.SpringMongoDbStorage;
import poussecafe.storage.internal.InternalStorage;

import static poussecafe.check.Checks.checkThatValue;

public class AddAggregateExecutor implements MojoExecutor {

    public static class Builder {

        private AddAggregateExecutor executor = new AddAggregateExecutor();

        public Builder sourceDirectory(File sourceDirectory) {
            this.sourceDirectory = sourceDirectory;
            return this;
        }

        private File sourceDirectory;

        public Builder packageName(String packageName) {
            executor.packageName = packageName;
            executor.aggregateDirectory = aggregatePackageDirectory(packageName);
            executor.modelDirectory = executor.aggregateDirectory;
            executor.adaptersDirectory = new File(executor.aggregateDirectory, "adapters");
            return this;
        }

        private File aggregatePackageDirectory(String packageName) {
            String[] packageSubDirectories = packageName.split("\\.");
            File aggregatePackageDirectory = new File(sourceDirectory.getAbsolutePath());
            for(int i = 0; i < packageSubDirectories.length; ++i) {
                aggregatePackageDirectory = new File(aggregatePackageDirectory, packageSubDirectories[i]);
            }
            return aggregatePackageDirectory;
        }

        public Builder name(String name) {
            executor.name = name;
            return this;
        }

        public Builder storageAdapters(Set<String> storageAdapters) {
            executor.storageAdapters = storageAdapters;
            return this;
        }

        public Builder missingAdaptersOnly(boolean missingAdaptersOnly) {
            executor.missingAdaptersOnly = missingAdaptersOnly;
            return this;
        }

        public AddAggregateExecutor build() {
            checkThatValue(executor.aggregateDirectory).notNull();
            checkThatValue(executor.name).notNull();

            executor.sourceWriter = new SourceWriter.Builder()
                    .modelPackageName(executor.packageName)
                    .adaptersPackageName(executor.packageName + ".adapters")
                    .name(executor.name)
                    .storageAdapters(executor.storageAdapters)
                    .build();

            return executor;
        }
    }

    private AddAggregateExecutor() {

    }

    private String packageName;

    private File aggregateDirectory;

    private File modelDirectory;

    private File adaptersDirectory;

    private String name;

    private SourceWriter sourceWriter;

    private Set<String> storageAdapters;

    private boolean missingAdaptersOnly;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            checkDoesNotExist();
            createAggregateDirectories();
            writeModelFiles();
            writeStorageAdaptersFiles();
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private void checkDoesNotExist() {
        if(!missingAdaptersOnly && aggregateDirectory.exists()) {
            throw new PousseCafeException("Aggregate directory " + aggregateDirectory.getAbsolutePath() + " already exists");
        }
    }

    private void createAggregateDirectories() {
        createDirectory(modelDirectory);
        createDirectory(adaptersDirectory);
    }

    private void createDirectory(File directory) {
        if(!directory.mkdirs() && (!directory.exists() || !directory.isDirectory())) {
            throw new PousseCafeException("Unable to create directory " + directory.getAbsolutePath());
        }
    }

    private void writeModelFiles() {
        if(!missingAdaptersOnly) {
            ModelSourceGenerator sourceGenerator = new ModelSourceGenerator.Builder()
                    .aggregateName(name)
                    .modelDirectory(modelDirectory)
                    .sourceWriter(sourceWriter)
                    .build();
            sourceGenerator.generate();
        }
    }

    private void writeStorageAdaptersFiles() {
        writeCommonStorageFiles();
        writeSpecificStorageFiles();
    }

    private void writeCommonStorageFiles() {
        CommonStorageSourceGenerator generator = new CommonStorageSourceGenerator.Builder()
                .aggregateName(name)
                .adaptersDirectory(adaptersDirectory)
                .sourceWriter(sourceWriter)
                .build();
        generator.generate();
    }

    private void writeSpecificStorageFiles() {
        Map<String, StorageSourceGeneratorBuilder> availableGenerators = availableGenerators();
        for(Entry<String, StorageSourceGeneratorBuilder> entry : availableGenerators.entrySet()) {
            if(storageAdapters.contains(entry.getKey())) {
                StorageSourceGenerator generator = entry.getValue()
                    .aggregateName(name)
                    .adaptersDirectory(adaptersDirectory)
                    .sourceWriter(sourceWriter)
                    .build();
                generator.generate();
            }
        }
    }

    private Map<String, StorageSourceGeneratorBuilder> availableGenerators() {
        Map<String, StorageSourceGeneratorBuilder> availableGenerators = new HashMap<>();
        availableGenerators.put(InternalStorage.NAME, new InternalStorageSourceGenerator.Builder());
        availableGenerators.put(SpringMongoDbStorage.NAME, new SpringMongoStorageSourceGenerator.Builder());
        return availableGenerators;
    }
}
