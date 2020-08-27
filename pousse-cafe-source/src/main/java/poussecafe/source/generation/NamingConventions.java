package poussecafe.source.generation;

import poussecafe.source.analysis.Name;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.Command;
import poussecafe.source.model.DomainEvent;

public class NamingConventions {

    public static Name aggregateIdentifierTypeName(Aggregate aggregate) {
        return new Name(aggregate.packageName(), aggregate.name() + "Id");
    }

    public static Name aggregateRootTypeName(Aggregate aggregate) {
        return new Name(aggregate.packageName(), aggregate.name());
    }

    public static Name aggregateFactoryTypeName(Aggregate aggregate) {
        return new Name(aggregate.packageName(), aggregate.name() + FACTORY_NAME_SUFFIX);
    }

    private static final String FACTORY_NAME_SUFFIX = "Factory";

    public static boolean isAggregateFactoryName(String typeName) {
        return typeName.endsWith(FACTORY_NAME_SUFFIX);
    }

    public static String aggregateNameFromFactory(String factoryName) {
        if(!isAggregateFactoryName(factoryName)) {
            throw new IllegalArgumentException("Given type name is not a factory name");
        }
        return nameWithoutSuffix(factoryName, FACTORY_NAME_SUFFIX);
    }

    public static String nameWithoutSuffix(String typeName, String suffix) {
        if(!typeName.endsWith(suffix)) {
            throw new IllegalArgumentException("Given type name must end with " + suffix);
        }
        return typeName.substring(0, typeName.length() - suffix.length());
    }

    public static Name aggregateRepositoryTypeName(Aggregate aggregate) {
        return new Name(aggregate.packageName(), aggregate.name() + REPOSITORY_NAME_SUFFIX);
    }

    private static final String REPOSITORY_NAME_SUFFIX = "Repository";

    public static boolean isAggregateRepositoryName(String typeName) {
        return typeName.endsWith(REPOSITORY_NAME_SUFFIX);
    }

    public static String aggregateNameFromRepository(String repositoryName) {
        if(!isAggregateRepositoryName(repositoryName)) {
            throw new IllegalArgumentException("Given type name is not a repository name");
        }
        return nameWithoutSuffix(repositoryName, REPOSITORY_NAME_SUFFIX);
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

    public static Name commandTypeName(Command command) {
        return new Name(command.packageName(), command.simpleName());
    }

    public static Name commandImplementationTypeName(Command command) {
        return new Name(command.packageName() + "." + ADAPTERS_PACKAGE_NAME, command.simpleName() + "Data");
    }

    public static Name eventTypeName(DomainEvent event) {
        return new Name(event.packageName(), event.simpleName());
    }

    public static Name eventImplementationTypeName(DomainEvent event) {
        return new Name(event.packageName() + "." + ADAPTERS_PACKAGE_NAME, event.simpleName() + "Data");
    }

    public static String runnerPackage(Aggregate aggregate) {
        return aggregate.packageName();
    }

    public static String processesPackageName(String basePackage) {
        return basePackage + ".process";
    }

    public static String commandsPackageName(String basePackage) {
        return basePackage + ".commands";
    }

    private NamingConventions() {

    }
}
