package poussecafe.doc.model.relation;

import java.util.Objects;
import poussecafe.domain.ValueObject;

public class RelationKey implements ValueObject {

    public RelationKey(String fromClass, String toClass) {
        Objects.requireNonNull(fromClass);
        this.fromClass = fromClass;

        Objects.requireNonNull(toClass);
        this.toClass = toClass;
    }

    private String fromClass;

    public String fromClass() {
        return fromClass;
    }

    private String toClass;

    public String toClass() {
        return toClass;
    }
}
