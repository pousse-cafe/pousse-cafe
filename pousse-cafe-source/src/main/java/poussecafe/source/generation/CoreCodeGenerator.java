package poussecafe.source.generation;

import java.nio.file.Path;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

public class CoreCodeGenerator extends AbstractCodeGenerator {

    public void generate(Aggregate aggregate) {
        addAggregateId(aggregate);
        addAggregateRoot(aggregate);
        addAggregateDataAccess(aggregate);
        addAggregateFactory(aggregate);
        addAggregateRepository(aggregate);
        addAttributesDefaultImplementation(aggregate);
    }

    private void addAggregateId(Aggregate aggregate) {
        var typeName = AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateRootEditor = new AggregateIdEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateRootEditor.edit();
    }

    private void addAggregateRoot(Aggregate aggregate) {
        var typeName = AggregateCodeGenerationConventions.aggregateRootTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateRootEditor = new AggregateRootEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateRootEditor.edit();
    }

    private void addAggregateDataAccess(Aggregate aggregate) {
        var typeName = AggregateCodeGenerationConventions.aggregateDataAccessTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateRootEditor = new AggregateDataAccessEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateRootEditor.edit();
    }

    private void addAggregateFactory(Aggregate aggregate) {
        var typeName = AggregateCodeGenerationConventions.aggregateFactoryTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateFactoryEditor = new AggregateFactoryEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateFactoryEditor.edit();
    }

    private void addAggregateRepository(Aggregate aggregate) {
        var typeName = AggregateCodeGenerationConventions.aggregateRepositoryTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateFactoryEditor = new AggregateRepositoryEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateFactoryEditor.edit();
    }

    private void addAttributesDefaultImplementation(Aggregate aggregate) {
        var typeName = AggregateCodeGenerationConventions.aggregateAttributesImplementationTypeName(aggregate);
        var compilationUnitEditor = compilationUnitEditor(typeName);
        var aggregateFactoryEditor = new AggregateAttributesImplementationEditor.Builder()
                .compilationUnitEditor(compilationUnitEditor)
                .aggregate(aggregate)
                .build();
        aggregateFactoryEditor.edit();
    }

    public static class Builder {

        private CoreCodeGenerator generator = new CoreCodeGenerator();

        public CoreCodeGenerator build() {
            requireNonNull(generator.sourceDirectory);
            return generator;
        }

        public Builder sourceDirectory(Path sourceDirectory) {
            generator.sourceDirectory = sourceDirectory;
            return this;
        }
    }

    private CoreCodeGenerator() {

    }
}
