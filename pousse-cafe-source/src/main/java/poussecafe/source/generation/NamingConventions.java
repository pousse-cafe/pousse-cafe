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

    private NamingConventions() {

    }
}
