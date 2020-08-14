package poussecafe.source.generation;

import poussecafe.source.analysis.Name;
import poussecafe.source.model.Aggregate;

public class AggregateCodeGenerationConventions {

    public static Name aggregateIdentifierTypeName(Aggregate aggregate) {
        return new Name(aggregate.packageName(), aggregate.name() + "Id");
    }

    public static Name aggregateRootTypeName(Aggregate aggregate) {
        return new Name(aggregate.packageName(), aggregate.name());
    }

    public static Name aggregateFactoryTypeName(Aggregate aggregate) {
        return new Name(aggregate.packageName(), aggregate.name() + "Factory");
    }

    public static Name aggregateRepositoryTypeName(Aggregate aggregate) {
        return new Name(aggregate.packageName(), aggregate.name() + "Repository");
    }

    public static Name aggregateDataAccessTypeName(Aggregate aggregate) {
        return new Name(aggregate.packageName(), aggregate.name() + "DataAccess");
    }

    public static Name aggregateAttributesQualifiedTypeName(Aggregate aggregate) {
        return new Name(aggregate.name(), ATTRIBUTES_CLASS_NAME);
    }

    public static final String ATTRIBUTES_CLASS_NAME = "Attributes";

    public static final String REPOSITORY_DATA_ACCESS_METHOD_NAME = "dataAccess";

    public static final String ADAPTERS_PACKAGE_NAME = "adapters";

    public static Name aggregateAttributesImplementationTypeName(Aggregate aggregate) {
        return new Name(adaptersPackageName(aggregate), aggregate.name() + ATTRIBUTES_CLASS_NAME);
    }

    public static String adaptersPackageName(Aggregate aggregate) {
        return aggregate.packageName() + "." + ADAPTERS_PACKAGE_NAME;
    }

    public static Name aggregateDataAccessImplementationTypeName(Aggregate aggregate, String storageName) {
        return new Name(adaptersPackageName(aggregate), aggregate.name() + storageName + "DataAccess");
    }

    private AggregateCodeGenerationConventions() {

    }
}
