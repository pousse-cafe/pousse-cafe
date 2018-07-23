package poussecafe.doc.model.relation;

import poussecafe.domain.ValueObject;

import static poussecafe.check.Checks.checkThatValue;

public class RelationKey implements ValueObject {

    public RelationKey(String fromClass, String toClass) {
        checkThatValue(fromClass).notNull();
        this.fromClass = fromClass;

        checkThatValue(toClass).notNull();
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
