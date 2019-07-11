package poussecafe.doc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import poussecafe.doc.model.boundedcontextdoc.BoundedContextDoc;
import poussecafe.doc.model.domainprocessdoc.DomainProcessDoc;
import poussecafe.doc.model.servicedoc.ServiceDoc;
import poussecafe.domain.ValueObject;

public class BoundedContext implements ValueObject {

    public static class Builder {

        private BoundedContext boundedContext = new BoundedContext();

        public Builder documentation(BoundedContextDoc documentation) {
            boundedContext.documentation = documentation;
            return this;
        }

        public Builder aggregates(List<Aggregate> aggregates) {
            boundedContext.aggregates = new ArrayList<>(aggregates);
            return this;
        }

        public Builder services(List<ServiceDoc> services) {
            boundedContext.services = new ArrayList<>(services);
            return this;
        }

        public Builder processes(List<DomainProcessDoc> processes) {
            boundedContext.processes = new ArrayList<>(processes);
            return this;
        }

        public BoundedContext build() {
            Objects.requireNonNull(boundedContext.documentation);
            Objects.requireNonNull(boundedContext.aggregates);
            Objects.requireNonNull(boundedContext.services);
            Objects.requireNonNull(boundedContext.processes);
            return boundedContext;
        }
    }

    private BoundedContext() {

    }

    private BoundedContextDoc documentation;

    public BoundedContextDoc documentation() {
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
