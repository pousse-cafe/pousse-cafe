package poussecafe.source.generation;

import poussecafe.source.analysis.Name;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.Command;
import poussecafe.source.model.DomainEvent;

public class NamingConventions {

    public static Name aggregateIdentifierTypeName(Aggregate aggregate) {
        return new Name(aggregate.packageName(), aggregate.simpleName() + "Id");
    }

    public static Name aggregateContainerTypeName(Aggregate aggregate) {
        return new Name(aggregate.packageName(), aggregate.simpleName());
    }

    public static Name aggregateRootTypeName(Aggregate aggregate) {
        if(aggregate.innerRoot()) {
            return new Name(aggregateContainerTypeName(aggregate).toString(), ROOT_SUFFIX);
        } else {
            return new Name(aggregate.packageName(), aggregate.simpleName() + ROOT_SUFFIX);
        }
    }

    private static final String ROOT_SUFFIX = "Root";

    public static Name aggregateFactoryTypeName(Aggregate aggregate) {
        return new Name(aggregate.packageName(), aggregate.simpleName() + FACTORY_NAME_SUFFIX);
    }

    private static final String FACTORY_NAME_SUFFIX = "Factory";

    public static String innerFactoryClassName() {
        return FACTORY_NAME_SUFFIX;
    }

    public static boolean isAggregateFactoryName(String typeName) {
        return typeName.endsWith(FACTORY_NAME_SUFFIX);
    }

    public static String aggregateNameFromSimpleFactoryName(String factoryName) {
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
        return new Name(aggregate.packageName(), aggregate.simpleName() + REPOSITORY_NAME_SUFFIX);
    }

    private static final String REPOSITORY_NAME_SUFFIX = "Repository";

    public static String innerRepositoryClassName() {
        return REPOSITORY_NAME_SUFFIX;
    }

    public static boolean isAggregateRepositoryName(String typeName) {
        return typeName.endsWith(REPOSITORY_NAME_SUFFIX);
    }

    public static String aggregateNameFromSimpleRepository(String repositoryName) {
        if(!isAggregateRepositoryName(repositoryName)) {
            throw new IllegalArgumentException("Given type name is not a repository name");
        }
        return nameWithoutSuffix(repositoryName, REPOSITORY_NAME_SUFFIX);
    }

    public static Name aggregateDataAccessTypeName(Aggregate aggregate) {
        return new Name(aggregate.packageName(), aggregate.simpleName() + "DataAccess");
    }

    public static Name aggregateAttributesQualifiedTypeName(Aggregate aggregate) {
        if(aggregate.innerRoot()) {
            return new Name(innerRootClassName(), ATTRIBUTES_CLASS_NAME);
        } else {
            return new Name(aggregateRootTypeName(aggregate).getIdentifier().toString(), ATTRIBUTES_CLASS_NAME);
        }
    }

    public static final String ATTRIBUTES_CLASS_NAME = "Attributes";

    public static final String REPOSITORY_DATA_ACCESS_METHOD_NAME = "dataAccess";

    public static final String ADAPTERS_PACKAGE_NAME = "adapters";

    public static Name aggregateAttributesImplementationTypeName(Aggregate aggregate) {
        return new Name(adaptersPackageName(aggregate), aggregate.simpleName() + ATTRIBUTES_CLASS_NAME);
    }

    public static String adaptersPackageName(Aggregate aggregate) {
        return aggregate.packageName() + "." + ADAPTERS_PACKAGE_NAME;
    }

    public static Name aggregateDataAccessImplementationTypeName(Aggregate aggregate, String storageName) {
        return new Name(adaptersPackageName(aggregate), aggregate.simpleName() + storageName + "DataAccess");
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

    public static String eventsPackageName(String basePackage) {
        return basePackage + ".model.events";
    }

    public static String innerRootClassName() {
        return ROOT_SUFFIX;
    }

    public static Name aggregateRootQualifiedIdentifier(Aggregate aggregate) {
        if(aggregate.innerRoot()) {
            return new Name(aggregateContainerTypeName(aggregate).getIdentifier().toString(), ROOT_SUFFIX);
        } else {
            return aggregateRootTypeName(aggregate).getIdentifier();
        }
    }

    private NamingConventions() {

    }
}
