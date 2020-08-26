package poussecafe.source.generation;

import poussecafe.source.analysis.Name;
import poussecafe.source.model.Aggregate;
import poussecafe.source.model.Command;
import poussecafe.source.model.DomainEvent;

public class CodeGenerationConventions {

    public static Name commandTypeName(Command command) {
        return new Name(command.packageName(), command.simpleName());
    }

    public static Name commandImplementationTypeName(Command command) {
        return new Name(command.packageName() + ".adapters", command.simpleName() + "Data");
    }

    public static Name eventTypeName(DomainEvent event) {
        return new Name(event.packageName(), event.simpleName());
    }

    public static Name eventImplementationTypeName(DomainEvent event) {
        return new Name(event.packageName() + ".adapters", event.simpleName() + "Data");
    }

    public static String runnerPackage(Aggregate aggregate) {
        return aggregate.packageName();
    }

    private CodeGenerationConventions() {

    }
}
