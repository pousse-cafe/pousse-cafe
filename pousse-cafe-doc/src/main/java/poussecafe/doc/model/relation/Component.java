package poussecafe.doc.model.relation;

import poussecafe.domain.ValueObject;

public class Component implements ValueObject {

    public Component(ComponentType type, String className) {
        this.type = type;
        this.className = className;
    }

    private ComponentType type;

    public ComponentType type() {
        return type;
    }

    private String className;

    public String className() {
        return className;
    }
}
