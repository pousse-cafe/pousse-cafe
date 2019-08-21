package poussecafe.doc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.moduledoc.ModuleDoc;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.domain.ValueObject;

public class Module implements ValueObject {

    public static class Builder {

        private Module module = new Module();

        public Builder documentation(ModuleDoc documentation) {
            module.documentation = documentation;
            return this;
        }

        public Builder aggregates(List<Aggregate> aggregates) {
            module.aggregates = new ArrayList<>(aggregates);
            return this;
        }

        public Builder services(List<ServiceDoc> services) {
            module.services = new ArrayList<>(services);
            return this;
        }

        public Builder processes(List<DomainProcessDoc> processes) {
            module.processes = new ArrayList<>(processes);
            return this;
        }

        public Module build() {
            Objects.requireNonNull(module.documentation);
            Objects.requireNonNull(module.aggregates);
            Objects.requireNonNull(module.services);
            Objects.requireNonNull(module.processes);
            return module;
        }
    }

    private Module() {

    }

    private ModuleDoc documentation;

    public ModuleDoc documentation() {
        return documentation;
    }

    private List<Aggregate> aggregates;

    public List<Aggregate> aggregates() {
        return aggregates;
    }

    private List<ServiceDoc> services;

    public List<ServiceDoc> services() {
        return services;
    }

    private List<DomainProcessDoc> processes;

    public List<DomainProcessDoc> processes() {
        return processes;
    }
}
