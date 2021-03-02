package poussecafe.source.generation;

import poussecafe.source.analysis.ClassName;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.Command;
import poussecafe.source.model.DomainEvent;

public class NamingConventions {

    public static ClassName aggregateIdentifierTypeName(Aggregate aggregate) {
        return new ClassName(aggregate.packageName(), aggregate.simpleName() + "Id");
    }

    public static ClassName aggregateContainerTypeName(Aggregate aggregate) {
        return new ClassName(aggregate.packageName(), aggregate.simpleName());
    }

    public static ClassName aggregateRootTypeName(Aggregate aggregate) {
        if(aggregate.innerRoot()) {
            return new ClassName(aggregateContainerTypeName(aggregate).toString(), ROOT_SUFFIX);
        } else {
            return new ClassName(aggregate.packageName(), aggregate.simpleName() + ROOT_SUFFIX);
        }
    }

    private static final String ROOT_SUFFIX = "Root";

    public static ClassName aggregateFactoryTypeName(Aggregate aggregate) {
        return new ClassName(aggregate.packageName(), aggregate.simpleName() + FACTORY_NAME_SUFFIX);
    }

    private static final String FACTORY_NAME_SUFFIX = "Factory";

    public static String innerFactoryClassName() {
        return FACTORY_NAME_SUFFIX;
    }

    public static boolean isStandaloneAggregateFactoryName(String typeName) {
        return typeName.endsWith(FACTORY_NAME_SUFFIX);
    }

    public static String aggregateNameFromSimpleFactoryName(String factoryName) {
        if(!isStandaloneAggregateFactoryName(factoryName)) {
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

    public static ClassName aggregateRepositoryTypeName(Aggregate aggregate) {
        return new ClassName(aggregate.packageName(), aggregate.simpleName() + REPOSITORY_NAME_SUFFIX);
    }

    private static final String REPOSITORY_NAME_SUFFIX = "Repository";

    public static String innerRepositoryClassName() {
        return REPOSITORY_NAME_SUFFIX;
    }

    public static boolean isStandaloneAggregateRepositoryName(String typeName) {
        return typeName.endsWith(REPOSITORY_NAME_SUFFIX);
    }

    public static String aggregateNameFromSimpleRepositoryName(String repositoryName) {
        if(!isStandaloneAggregateRepositoryName(repositoryName)) {
            throw new IllegalArgumentException("Given type name is not a repository name");
        }
        return nameWithoutSuffix(repositoryName, REPOSITORY_NAME_SUFFIX);
    }

    public static ClassName aggregateDataAccessTypeName(Aggregate aggregate) {
        if(aggregate.innerRepository()) {
            return aggregateContainerTypeName(aggregate)
                    .withLastSegment(innerRepositoryClassName())
                    .withLastSegment(innerDataAccessTypeName());
        } else {
            return aggregateRepositoryTypeName(aggregate).withLastSegment(innerDataAccessTypeName());
        }
    }

    public static String innerDataAccessTypeName() {
        return DATA_ACCESS_SUFFIX;
    }

    private static final String DATA_ACCESS_SUFFIX = "DataAccess";

    public static boolean isDataAccessDefinitionName(ClassName className) {
        return className.simple().endsWith(DATA_ACCESS_SUFFIX);
    }

    public static ClassName aggregateAttributesQualifiedTypeName(Aggregate aggregate) {
        if(aggregate.innerRoot()) {
            return new ClassName(innerRootClassName(), ATTRIBUTES_CLASS_NAME);
        } else {
            return new ClassName(aggregateRootTypeName(aggregate).getIdentifier().toString(), ATTRIBUTES_CLASS_NAME);
        }
    }

    public static final String ATTRIBUTES_CLASS_NAME = "Attributes";

    public static final String REPOSITORY_DATA_ACCESS_METHOD_NAME = "dataAccess";

    public static final String ADAPTERS_PACKAGE_NAME = "adapters";

    public static ClassName aggregateAttributesImplementationTypeName(AggregatePackage aggregate) {
        return new ClassName(adaptersPackageName(aggregate), aggregate.aggregateName() + ATTRIBUTES_CLASS_NAME);
    }

    public static String adaptersPackageName(AggregatePackage aggregate) {
        return aggregate.packageName() + "." + ADAPTERS_PACKAGE_NAME;
    }

    public static ClassName aggregateDataAccessImplementationTypeName(AggregatePackage aggregate, String storageName) {
        return new ClassName(adaptersPackageName(aggregate), aggregate.aggregateName() + storageName + DATA_ACCESS_SUFFIX);
    }

    public static boolean isDataAccessImplementationName(String storageName, ClassName name) {
        return name.simple().endsWith(storageName + DATA_ACCESS_SUFFIX);
    }

    public static ClassName commandTypeName(Command command) {
        return new ClassName(command.packageName(), command.simpleName());
    }

    public static ClassName commandImplementationTypeName(Command command) {
        return new ClassName(command.packageName() + "." + ADAPTERS_PACKAGE_NAME, command.simpleName() + MESSAGE_IMPLEMENTATION_SUFFIX);
    }

    private static final String MESSAGE_IMPLEMENTATION_SUFFIX = "Data";

    public static boolean isMessageImplementationName(ClassName className) {
        return className.simple().endsWith(MESSAGE_IMPLEMENTATION_SUFFIX);
    }

    public static ClassName eventTypeName(DomainEvent event) {
        return new ClassName(event.packageName(), event.simpleName());
    }

    public static ClassName eventImplementationTypeName(DomainEvent event) {
        return new ClassName(event.packageName() + "." + ADAPTERS_PACKAGE_NAME, event.simpleName() + MESSAGE_IMPLEMENTATION_SUFFIX);
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

    public static ClassName innerAggregateRootIdentifier(Aggregate aggregate) {
        if(aggregate.innerRoot()) {
            return new ClassName(aggregateContainerTypeName(aggregate).getIdentifier().toString(), ROOT_SUFFIX);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static String aggregateNameFromSimpleRootName(String simpleName) {
        if(!isStandaloneAggregateRootName(simpleName)) {
            throw new IllegalArgumentException("Given type name is not a valid root name: " + simpleName);
        }
        return nameWithoutSuffix(simpleName, ROOT_SUFFIX);
    }

    public static boolean isStandaloneAggregateRootName(String typeName) {
        return typeName.endsWith(ROOT_SUFFIX);
    }

    public static boolean isInnerAggregateRootName(String simpleName) {
        return innerRootClassName().equals(simpleName);
    }

    public static boolean isInnerAggregateFactoryName(String simpleName) {
        return innerFactoryClassName().equals(simpleName);
    }

    public static boolean isInnerAggregateRepositoryName(String simpleName) {
        return innerRepositoryClassName().equals(simpleName);
    }

    public static String aggregateNameFromContainer(String simpleName) {
        return simpleName;
    }

    public static boolean isEntityImplementationName(ClassName className) {
        return className.simple().endsWith(ATTRIBUTES_CLASS_NAME);
    }

    private NamingConventions() {

    }
}
