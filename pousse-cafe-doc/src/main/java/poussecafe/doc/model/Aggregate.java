package poussecafe.doc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.processstepdoc.ProcessStepDoc;
import poussecafe.doc.model.vodoc.ValueObjectDoc;

public class Aggregate {

    public static class Builder {

        private Aggregate aggregate = new Aggregate();

        public Builder documentation(AggregateDoc documentation) {
            aggregate.documentation = documentation;
            return this;
        }

        public Builder entities(List<EntityDoc> entities) {
            aggregate.entities = new ArrayList<>(entities);
            return this;
        }

        public Builder valueObjects(List<ValueObjectDoc> valueObjects) {
            aggregate.valueObjects = new ArrayList<>(valueObjects);
            return this;
        }

        public Builder processSteps(List<ProcessStepDoc> processSteps) {
            aggregate.processSteps = new ArrayList<>(processSteps);
            return this;
        }

        public Aggregate build() {
            Objects.requireNonNull(aggregate.documentation);
            Objects.requireNonNull(aggregate.entities);
            Objects.requireNonNull(aggregate.valueObjects);
            Objects.requireNonNull(aggregate.processSteps);
            return aggregate;
        }
    }

    private Aggregate() {

    }

    private AggregateDoc documentation;

    public AggregateDoc documentation() {
        return documentation;
    }

    private List<EntityDoc> entities;

    public List<EntityDoc> entities() {
        return entities;
    }

    private List<ValueObjectDoc> valueObjects;

    public List<ValueObjectDoc> valueObjects() {
        return valueObjects;
    }

    private List<ProcessStepDoc> processSteps;

    public List<ProcessStepDoc> processSteps() {
        return processSteps;
    }
}
