package poussecafe.doc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import poussecafe.doc.model.aggregatedoc.AggregateDoc;
import poussecafe.doc.model.entitydoc.EntityDoc;
import poussecafe.doc.model.vodoc.ValueObjectDoc;

public class Aggregate {

    public static class Builder {

        private Aggregate boundedContext = new Aggregate();

        public Builder documentation(AggregateDoc documentation) {
            boundedContext.documentation = documentation;
            return this;
        }

        public Builder entities(List<EntityDoc> entities) {
            boundedContext.entities = new ArrayList<>(entities);
            return this;
        }

        public Builder valueObjects(List<ValueObjectDoc> valueObjects) {
            boundedContext.valueObjects = new ArrayList<>(valueObjects);
            return this;
        }

        public Aggregate build() {
            Objects.requireNonNull(boundedContext.documentation);
            Objects.requireNonNull(boundedContext.entities);
            Objects.requireNonNull(boundedContext.valueObjects);
            return boundedContext;
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
}
